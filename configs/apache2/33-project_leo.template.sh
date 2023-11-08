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

# The ProxyRequests directive should usually be set off when using ProxyPass.
# See: https://httpd.apache.org/docs/2.4/mod/mod_proxy.html#proxypass
ProxyRequests Off

# This is the localhost port and path that Project Leo is listening at.
<Proxy http://localhost:${PORT}*>
  Require all granted
</Proxy>

# This is the public-facing web address that Project Leo will be available at.
<Location "/">
  # Project Leo will handle authentication. Apache allows anyone.
  Require all granted

  ProxyPass        "http://localhost:${PORT}/" nocanon retry=0
  ProxyPassReverse "http://localhost:${PORT}/"
  ProxyPassReverse "${HTTPS}://${SUBDOMAIN}.${DOMAIN}/"

  # Header edit Location "^http://localhost:${PORT}(.*)" "${HTTPS}://${SUBDOMAIN}.${DOMAIN}/\$1"
</Location>

EOF
