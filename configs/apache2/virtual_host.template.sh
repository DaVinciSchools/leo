#!/bin/bash

set -o nounset -o errexit
PATH=/sbin:/bin:/usr/sbin:/usr/bin
unalias -a

# Requires the following substitutions:
# - \${SUBDOMAIN} = "${SUBDOMAIN}" (e.g., www)
# - \${HTTPS} = "${HTTPS}" (e.g., http or https) - http uses localhost
HTTPS_PORT=$([ "${HTTPS}" = 'https' ] && echo 443 || echo 80)
DOMAIN=$([ "${HTTPS}" = 'https' ] && hostname -f || echo localhost)

cat <<EOF

$([ "${HTTPS}" = 'https' ] && cat <<SSL_EOF

  # Permanently redirect any HTTP traffic on any hosts to HTTPS.
  RewriteEngine On
  RewriteCond %{HTTPS} !=on
  RewriteCond %{HTTP_HOST} ^$([ "${HTTPS}" = 'https' ] && echo "${SUBDOMAIN}".)${DOMAIN}\$
  # We need to allow this one exception to allow letsencrypt to renew HTTPS
  # certificates. Letsencrypt verifies that you really own the host by
  # placing unique files in this folder and then verifying that they are
  # served by your server at that host. So, we need to allow non-HTTPS to
  # serve those files.
  RewriteCond %{REQUEST_URI} !^/?\.well-known/acme-challenge/
  RewriteRule ^ http://%{HTTP_HOST}%{REQUEST_URI} [END,NE,R=permanent]

SSL_EOF
)

# This is the virtual host (i.e., website) for a subdomain. It includes
# any files named ${SUBDOMAIN}.*.conf.subdomain.
<VirtualHost *:${HTTPS_PORT}>
  # The domain that this configuration listens to.
  ServerName ${SUBDOMAIN}.${DOMAIN}

  # Set the administrator information.
  ServerAdmin webmaster@${DOMAIN}

  # Create logs in files starting with the subdomain.
  ErrorLog \${APACHE_LOG_DIR}/${SUBDOMAIN}.error.log
  CustomLog \${APACHE_LOG_DIR}/${SUBDOMAIN}.access.log combined

  $([ "${HTTPS}" = 'https' ] && cat <<SSL_EOF

    # Include the Apache configuration from letsencrypt to enable HTTPS.
    Include /etc/letsencrypt/options-ssl-apache.conf

    # Include the SSL certificate files from letsencrypt.
    SSLCertificateFile /etc/apache2/ssl/cert.pem
    SSLCertificateKeyFile /etc/apache2/ssl/privkey.pem
    SSLCertificateChainFile /etc/apache2/ssl/fullchain.pem

SSL_EOF
)

  # These browser are broken for the standard SSL protocol. These
  # configurations adjust the protocol to be compatible with the
  # specific browsers.
  BrowserMatch "MSIE [2-6]" \
    nokeepalive \
    ssl-unclean-shutdown \
    downgrade-1.0 \
    force-response-1.0
  BrowserMatch "MSIE [17-9]" \
    ssl-unclean-shutdown

  # Point the default served files to /var/www/html and index.html.
  Alias / /var/www/html

  # Include content for this virtual host.
  Include /etc/apache2/sites-enabled/${SUBDOMAIN}.*.conf.subdomain
</VirtualHost>

EOF

touch /etc/apache2/sites-enabled/"${SUBDOMAIN}".virtual_host_default.conf.subdomain
