#!/bin/bash

set -o nounset -o errexit
PATH=/sbin:/bin:/usr/sbin:/usr/bin
unalias -a

# Requires the following substitutions:
# - \${HTTPS} = "${HTTPS}" ('https' or 'http')
# - \${PORT} = "${PORT}" (e.g., 12345)
# - \${SUBDOMAIN} = "${SUBDOMAIN}" (e.g., www)
DOMAIN=$([ "${HTTPS}" = 'https' ] && hostname -f || echo localhost)

cat <<EOF

# This is the localhost port and path that Project Leo is listening at.
<Proxy http://localhost:${PORT}*>
  Require all granted
</Proxy>

# This is the public-facing web address that Project Leo will be available at.
<LocationMatch "^/(?!\~).*">
  # Project Leo will handle authentication. Apache allows anyone.
  Require all granted

  # This tells Apache to translate ${HTTPS}://$([ "${HTTPS}" = 'https' ] && echo "${SUBDOMAIN}".)${DOMAIN}
  # URLs to and from Project Leo's http://localhost:${PORT} URLs.
  ProxyPass http://localhost:${PORT} retry=0 nocanon
  ProxyPassReverse http://localhost:${PORT}
  ProxyPassReverse ${HTTPS}://%{HTTP_HOST}
</LocationMatch>

EOF
