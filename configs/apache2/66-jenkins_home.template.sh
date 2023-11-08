#!/bin/bash

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

# Jenkins requirements for running as a proxy are at:
# https://www.jenkins.io/doc/book/system-administration/reverse-proxy-configuration-troubleshooting/

# Jenkins requirement for running a reverse proxy.
ProxyRequests Off
AllowEncodedSlashes NoDecode

# This is the localhost port and path that Jenkins is listening at.
<Proxy "http://localhost:${PORT}/~${USER}/jenkins*">
  Require all granted
</Proxy>

# This is the public-facing web address that Jenkins will be available at.
<Location "/~${USER}/jenkins">
  # Jenkins will handle authentication. Apache allows anyone.
  Require all granted

  # Jenkins requirement for running a reverse proxy.
  ProxyPass        "http://localhost:${PORT}/~${USER}/jenkins" nocanon retry=0
  ProxyPassReverse "http://localhost:${PORT}/~${USER}/jenkins"
  ProxyPassReverse "${HTTPS}://${SUBDOMAIN}.${DOMAIN}/~${USER}/jenkins"

  # Jenkins requirement for running a reverse proxy.
  # This seems to be set by ProxyPass.
  # RequestHeader set X-Forwarded-Proto "${HTTPS}"
  RequestHeader set X-Forwarded-Host "${SUBDOMAIN}.${DOMAIN}"
  RequestHeader set X-Forwarded-Port "${HTTPS_PORT}"
  # RequestHeader set X-Forwarded-User %{RU}e

</Location>

EOF
