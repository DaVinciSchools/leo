#!/bin/bash -x

set -o nounset -o errexit
PATH=/sbin:/bin:/usr/sbin:/usr/bin
unalias -a

# Requires the following substitutions:
# - \${PORT} = "${PORT}" (e.g., 12345)
# - \${USER} = "${USER}" (e.g., staging)
# - \${SUBDOMAIN} = "${SUBDOMAIN}" (e.g., www)
# - \${HTTPS} = "${HTTPS}" (e.g., http or https) - http uses localhost
HTTPS_PORT=$([ "${HTTPS}" = 'https' ] && echo 443 || echo 80)
DOMAIN=$([ "${HTTPS}" = 'https' ] && hostname -f || echo localhost)

cat <<EOF

# Jenkins requires the following directive to be set.
AllowEncodedSlashes NoDecode
# ProxyRequests Off

# This is the localhost port and path that Jenkins is listening at.
<Proxy "http://localhost:${PORT}/~${USER}/jenkins*">
  Require all granted
</Proxy>

# This is the public-facing web address that Jenkins will be available at.
<Location "/~${USER}/jenkins">
  # Jenkins will handle authentication. Apache allows anyone.
  Require all granted

  # This tells Apache to translate ${HTTPS}://$([ "${HTTPS}" = 'https' ] && echo "${SUBDOMAIN}".)${DOMAIN}/~${USER}/jenkins
  # URLs to and from Jenkins' http://localhost:${PORT}/~${USER}/jenkins URLs.
  ProxyPass "http://localhost:${PORT}/~${USER}/jenkins" retry=0 nocanon
  ProxyPassReverse "http://localhost:${PORT}/~${USER}/jenkins"
  ProxyPassReverse "${HTTPS}://%{HTTP_HOST}/~${USER}/jenkins"

  # Jenkins requirements for running as a proxy. See:
  # https://www.jenkins.io/doc/book/system-administration/reverse-proxy-configuration-troubleshooting/
  RequestHeader set X-Forwarded-User %{RU}e
  RequestHeader set X-Forwarded-Proto "${HTTPS}"
  RequestHeader set X-Forwarded-Port "${HTTPS_PORT}"
</Location>

EOF
