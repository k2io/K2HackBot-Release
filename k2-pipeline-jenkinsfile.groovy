properties([[$class: 'RebuildSettings', autoRebuild: false, rebuildDisabled: false], parameters([
  string(defaultValue: '192.168.5.247', description: 'The IP of the machine where the entire setup will run including your application.', name: 'REMOTE_HOST_IP', trim: false),
  string(defaultValue: 'root', description: 'The user of the machine(mentioned above) which is used to do SSH from Jenkins server.', name: 'REMOTE_HOST_USER', trim: false),
  string(defaultValue: '', description: 'The password of the machine(mentioned above) which is used to do SSH from Jenkins server.', name: 'REMOTE_HOST_PASSWORD', trim: false),
  string(defaultValue: '', description: '''This field is used to update/downgrade the existing setup of the K2HackBot. 

Refer this to get all versions of K2HackBot: https://github.com/k2io/K2HackBot-Release/releases

Keep this field as empty to download the latest K2HackBot bundle''', name: 'K2HACKBOT_BUNDLE_URL', trim: false),

  string(defaultValue: '/opt/k2-ic', description: '''The directory path present inside the remote machine where you wish to download all the K2 collectors.

By default, this is set to /opt/k2-ic''', name: 'K2_COLLECTORS_PATH', trim: false),

  string(defaultValue: 'installer@k2io.com', description: '', name: 'K2_EMAIL', trim: false),
  string(defaultValue: '', description: '', name: 'K2_PASSWORD', trim: false),
  string(defaultValue: 'IAST', description: '', name: 'K2_GROUP_NAME', trim: false),
  choice(choices: ['IAST', 'QA', 'STAGING', 'DEVELOPMENT'], description: '', name: 'K2_ENV'),
  string(defaultValue: 'https://k2io.net', description: '', name: 'K2_CLOUD', trim: false),
  string(defaultValue: '', description: '''The absolute path of the application start script present on the remote machine.

NOTE: In case if both the fields are specified - "App_Start_Script" and "App_Start_Command", preference will be given to the "App_Start_Script" to launch the application setup.''', name: 'APP_START_SCRIPT', trim: false),
  string(defaultValue: '', description: '''The command to run the application setup.

NOTE: In case if both the fields are specified - "App_Start_Script" and "App_Start_Command", preference will be given to the "App_Start_Script" to launch the application setup.''', name: 'APP_START_COMMAND', trim: false),
  string(defaultValue: '', description: '', name: 'TRIGGER_QA_TESTS', trim: false),
  string(defaultValue: '', description: 'Application URls to be crawled.', name: 'APPLICATION_URL', trim: false),
  string(defaultValue: '', description: '''Provide ContainerID/PID of Application Hosted in Docker/Host environment.
Provide application Identifier string within single quotes in given format: 
                                  
'{"containerid":"application_container_id","pid":application_process_id}'
    ''', name: 'APPLICATION_IDENTIFIER', trim: false),
  choice(choices: ['false', 'true'], description: 'If your applications requires authentication using login.', name: 'IS_AUTH_REQUIRED'),
  string(defaultValue: '', description: '''If your Application needs a user to be authenticated using login, we need identifier to do that ourself.
Provide identifier  for Application form fields in format as string:

'{"username": {"identification": "user_field_id","value": "user_name"},"password": {"identification": "password_field_id","value": "password"},"submit": {"identification": "submit_button_id","value": "Nothing"} }'
                                  
You can find Guide to Do so on following git repository https://github.com/k2io/K2ADS''', name: 'APPLICATION_LOGIN_IDENTIFIER', trim: false),

  string(defaultValue: '', description: 'Application URLs to be skipped for crawling.', name: 'IGNORE_URL', trim: false),
  string(defaultValue: '', description: 'Application Domains to be used for crawling.', name: 'ALLOWED_DOMAIN', trim: false)
  ])])

pipeline {
    agent any
    
    stages {
        
        stage('Setup K2HackBot') {
            
            steps{
                script{
                    
                    k2hackbot_install_script_download_command="wget https://raw.githubusercontent.com/k2io/K2HackBot-Release/k2hackbot-automation-demo/install-k2hackbot.sh -P /tmp"
                    k2hackbot_install_command="bash /tmp/install-k2hackbot.sh ${K2HACKBOT_BUNDLE_URL}"

                    echo ">> Removing K2HackBot installation script if already present inside the /tmp directory.\n"
                    sh("sshpass -p ${REMOTE_HOST_PASSWORD} ssh -o StrictHostKeyChecking=no ${REMOTE_HOST_USER}@${REMOTE_HOST_IP} 'rm -f /tmp/install-k2hackbot.sh'")
                    echo ">> Downloading latest K2HackBot installation script.\n"
                    sh("sshpass -p ${REMOTE_HOST_PASSWORD} ssh -o StrictHostKeyChecking=no ${REMOTE_HOST_USER}@${REMOTE_HOST_IP} $k2hackbot_install_script_download_command")
                    echo ">> Proceeding with the K2HackBot setup.\n"
                    sh("sshpass -p ${REMOTE_HOST_PASSWORD} ssh -o StrictHostKeyChecking=no ${REMOTE_HOST_USER}@${REMOTE_HOST_IP} $k2hackbot_install_command")
                
                    if ( ! "$APP_START_SCRIPT" ){
                        temp_appstartscript=null
                    } else{
                        temp_appstartscript="\"${APP_START_SCRIPT}\""
                    }
                    

                    if ( ! "$APP_START_COMMAND" ){
                        temp_appstartcommand=null
                    }else{
                        temp_appstartcommand="\"${APP_START_COMMAND}\""
                    }
                    

                    if ( ! "$APPLICATION_URL" ){
                        temp_appurl=null
                    }else{
                        temp_appurl="\"${APPLICATION_URL}\""
                    }
                   

                    if ( ! "$K2_COLLECTORS_PATH" ){
                        temp_k2icdirpath="\"/opt/k2-ic\""
                    }else{
                        temp_k2icdirpath="\"${K2_COLLECTORS_PATH}\""
                    }
                   

                    if ( ! "$APPLICATION_IDENTIFIER" ){
                        temp_appidf=null
                    }else{
                        temp_appidf="\"${APPLICATION_IDENTIFIER}\""
                    }
                   

                    if ( ! "$APPLICATION_LOGIN_IDENTIFIER" ){
                        temp_app_login_idf=null
                    }else{
                        temp_app_login_idf="\"${APPLICATION_LOGIN_IDENTIFIER}\""
                    }
                   

                    if ( ! "$IGNORE_URL" ){
                        temp_ignore_url=""
                    }else{
                        temp_ignore_url="\"${IGNORE_URL}\""
                    }
                   

                    if ( ! "$ALLOWED_DOMAIN" ){
                        temp_allowed_domain=""
                    }else{
                        temp_allowed_domain="\"${ALLOWED_DOMAIN}\""
                    }
                   

                    if ( ! "$K2_EMAIL" ){
                        temp_k2email=null
                    }else{
                        temp_k2email="\"${K2_EMAIL}\""
                    }
                   

                    if ( ! "$K2_PASSWORD" ){
                        temp_k2password=null
                    }else{
                        temp_k2password="\"${K2_PASSWORD}\""
                    }
                   

                    if ( ! "$K2_GROUP_NAME" ){
                        temp_k2_groupname=null
                    }else{
                        temp_k2_groupname="\"${K2_GROUP_NAME}\""
                    }
                   

                    if ( ! "$K2_ENV" ){
                        temp_k2env=null
                    }else{
                        temp_k2env="\"${K2_ENV}\""
                    }
                   

                    if ( ! "$K2_CLOUD" ){
                        temp_k2cloud=null
                    }else{
                        temp_k2cloud="\"${K2_CLOUD}\""
                    }
                    
                    def config_file = """
                    {
                        "appstartscript": ${temp_appstartscript}, 
                        "applicationurl": [${temp_appurl}],
                        "applicationidentifier": ${temp_appidf},
                        "isauthrequired": ${IS_AUTH_REQUIRED},
                        "applicationloginidentifier": ${temp_app_login_idf},
                        "ignoreurl": [${temp_ignore_url}],
                        "alloweddomain": [${temp_allowed_domain}],
                        "k2email": ${temp_k2email},
                        "k2password": ${temp_k2password},
                        "k2groupname": ${temp_k2_groupname},
                        "k2groupenv": ${temp_k2env},
                        "k2cloud": ${temp_k2cloud},
                        "k2agentdeploymenttype": "docker",
                        "k2icdirectorypath": ${temp_k2icdirpath},
                        "appstartcommand": ${temp_appstartcommand}
                    }
                    """
                    writeFile file: 'k2hackbot_config.json', text:config_file

                    sh 'ls -lah'
                    sh 'cat k2hackbot_config.json'
                    
                    echo "> Copying k2hackbot_config.json to /tmp/K2HackBot on the Remote Host: ${REMOTE_HOST_IP}\n"
                    sh("sshpass -p ${REMOTE_HOST_PASSWORD} scp ./k2hackbot_config.json ${REMOTE_HOST_USER}@${REMOTE_HOST_IP}:/tmp/K2HackBot")
                        
                }
            }
        }
        
        stage('Setup k2agent') {
            steps{
                script{
                    
                    sh("sshpass -p ${REMOTE_HOST_PASSWORD} ssh -o StrictHostKeyChecking=no ${REMOTE_HOST_USER}@${REMOTE_HOST_IP} '/tmp/K2HackBot/bin/k2hackbot deploy-k2component --config /tmp/K2HackBot/k2hackbot_config.json'")
                    
                }   
            }
        }

        stage('Launch User Application') {
            steps{
                script{
                    
                        sh("sshpass -p ${REMOTE_HOST_PASSWORD} ssh -o StrictHostKeyChecking=no ${REMOTE_HOST_USER}@${REMOTE_HOST_IP} '/tmp/K2HackBot/bin/k2hackbot start-application --config /tmp/K2HackBot/k2hackbot_config.json'")
                    }   
                
            }
        }
        stage('Run QA Test cases') {
            steps{
                script{                    
                        sh("sshpass -p ${REMOTE_HOST_PASSWORD} ssh -o StrictHostKeyChecking=no ${REMOTE_HOST_USER}@${REMOTE_HOST_IP} '$TRIGGER_QA_TESTS'")
                    }   
            }
        }
        stage('Crawl Application') {
            steps{
                script{                    
                        sh("sshpass -p ${REMOTE_HOST_PASSWORD} ssh -o StrictHostKeyChecking=no ${REMOTE_HOST_USER}@${REMOTE_HOST_IP} '/tmp/K2HackBot/bin/k2hackbot crawl-web-application --config /tmp/K2HackBot/k2hackbot_config.json'")
                    }   
            }
        }
        stage('Display Results') {
            steps{
                script{                    
                        sh("sshpass -p ${REMOTE_HOST_PASSWORD} ssh -o StrictHostKeyChecking=no ${REMOTE_HOST_USER}@${REMOTE_HOST_IP} '/tmp/K2HackBot/bin/k2hackbot extract-results --config /tmp/K2HackBot/k2hackbot_config.json'")
                    }   
            }
        }

    }
}

