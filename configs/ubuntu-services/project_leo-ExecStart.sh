#!/bin/bash

set -o nounset -o errexit
PATH=/sbin:/bin:/usr/sbin:/usr/bin
unalias -a

# Remove all of Apache's configurations and restart Apache.
rm -f /etc/apache2/sites-enabled/*-project_leo.template.sh.conf.subdomain
service apache2 reload || true

# Iterate over the home directories and see if they have a file
# named ".run_project_leo". If so, and if there is a branch by
# that user's name, start Project Leo for that use.
PIDS=""
for HOME_DIR in /home/*; do

  [ ! -f "${HOME_DIR}/.run_project_leo" ] && continue;

  LEO_USER="$(basename "${HOME_DIR}")"
  BRANCH="${LEO_USER}"
  BRANCH_DIR="/home/build/.project_leo/branches/${BRANCH}"

  [ ! -d "${BRANCH_DIR}" ] && continue;
  [ ! -L "${BRANCH_DIR}/latest" ] && continue;

  LEO_HOME_DIR="${HOME_DIR}/.project_leo/run/${BRANCH}"
  SUBDOMAIN="${LEO_USER}"
  HTTPS="$([[ "$(hostname -f)" =~ project[-_]?leo ]] && echo https || echo http)"
  PORT="$(/root/project_leo/configs/ubuntu-services/get_open_port.py)"
  DEBUG_PORT="$(/root/project_leo/configs/ubuntu-services/get_open_port.py)"

  FILE=(/etc/apache2/sites-available/??-project_leo.template.sh)
  HTTPS="${HTTPS}" \
  PORT="${PORT}" \
  SUBDOMAIN="${SUBDOMAIN}" \
      "${FILE[0]}" \
      > "/etc/apache2/sites-enabled/${SUBDOMAIN}.$(basename "${FILE[0]}").conf.subdomain"

  HTTPS="${HTTPS}" \
  SUBDOMAIN="${SUBDOMAIN}" \
      /etc/apache2/sites-available/virtual_host.template.sh \
      > "/etc/apache2/sites-enabled/${SUBDOMAIN}.virtual_host.conf"

  cat <<EOF | sudo -u "${LEO_USER}" bash -s
    mkdir -p "${LEO_HOME_DIR}"
    echo "${PORT}" >"${LEO_HOME_DIR}/server.port"
    echo "${DEBUG_PORT}" >"${LEO_HOME_DIR}/server.debug.port"
EOF

  # For debugging, add the following line to the executable statement below.
  # -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=localhost:"$(cat "${LEO_HOME_DIR}/server.debug.port")" \

  while true; do
    umask 077
    cat <<EOF | sudo -u "${LEO_USER}" bash -x -s
      $(which java) \
      -jar "$(realpath "${BRANCH_DIR}"/latest/project-leo-server-*.jar)" \
      --server.port="$(cat "${LEO_HOME_DIR}/server.port")" \
      >> "${LEO_HOME_DIR}/server.log" 2>&1 || true
EOF
    echo Restarting Project Leo for user "${LEO_USER}".
    sleep 5
  done &
  PIDS="${PIDS},$!"
done

# Restart Apache with the new configurations.
service apache2 reload

# shellcheck disable=SC2064
trap "pkill -P ${PIDS:1}; exit 0" TERM
wait ${PIDS//,/ }
