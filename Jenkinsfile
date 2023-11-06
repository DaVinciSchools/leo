pipeline {
  agent any
  tools {
    maven 'Maven 3'
    git 'Default'
    jdk 'JDK 17'
  }
  environment {
    BRANCH_DIR="${HOME}/.project_leo/branches/${BRANCH_NAME}"
    RUN_DIR="${BRANCH_DIR}/targets/${GIT_COMMIT}"
    MVN_REPO="${BRANCH_DIR}/.m2/repository"
    USER="${BRANCH_NAME}"
  }
  options {
    ansiColor('gnome-terminal')
  }
  stages {
    stage('Build') {
      steps{
        retry(2) {
          withMaven(\
              globalMavenSettingsConfig: '',
              jdk: 'JDK 17',
              maven: 'Maven 3',
              mavenLocalRepo: '${MVN_REPO}',
              mavenSettingsConfig: '',
              traceability: true) {
            sh "mvn --batch-mode -Dstyle.color=always clean verify"
          }
        }
      }
    }
    stage('Store Server Artifacts') {
      steps {
        sh label: 'Store Server Artifacts', script: '''
            # Copy built files to the server payload.
            /usr/bin/rm -rf "${RUN_DIR}" || true
            /usr/bin/mkdir -p "${RUN_DIR}"
            /usr/bin/cp -r "${WORKSPACE}"/server/target/project-leo-server-* "${RUN_DIR}"
            echo "${BUILD_URL}" > "${RUN_DIR}/build_url.txt"
            '''
        fingerprint '${WORKSPACE}/server/target/project-leo-server-*.jar'
        fingerprint '${WORKSPACE}/server/target/project-leo-server-*/*.jar'
      }
    }
    stage('Deploy Server') {
      steps {
        sh label: 'Deploy Server', script: '''
            /usr/bin/ln --symbolic --relative --force --no-dereference \
                "${RUN_DIR}" "${BRANCH_DIR}/latest"
            /usr/bin/sudo /usr/bin/systemctl start "restart_project_leo@${USER}.service" || (
                cat <<EOF
Failed to restart Project Leo service for ${USER}.
Do you have the following in your /etc/sudoers.d folder?
$(whoami) ALL=NOPASSWD: /bin/systemctl start restart_project_leo@[a-z]*.service
EOF
              exit 1
            )
            '''
      }
    }
  }
}