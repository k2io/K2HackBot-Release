Steps to import K2-Pipeline-Script
=================================

### Create a new Pipeline Job.

- Click "New Item" from the let menu in the jenkins.
- Enter the Pipeline Job Name and select the job type as "Pipeline". Click "OK" once done.
- Once the pipeline job is created, you'll be reirected to the pipeline configuration page.

### Add the K2 Jenkins Pipeline script in the newly created Pipeline Job.
	
- Download the pipeline script from here: https://raw.githubusercontent.com/k2io/K2HackBot-Release/k2hackbot-automation-demo/k2-pipeline-jenkinsfile.groovy
- Pasted this script in the "Pipeline" scetion of your newly created pipeline job.
- Save the changes

### Run a sample build to import all the parameters in the job.

- Headover to the new pipeline and click "Build Now".
- This will trigger a sample build which is expected to be failed.
- Once this sample build is completed, reload the page.
- This time, you'll see that the "Build Now" link has changed to "Build with Parameters"
- Click on "Build with Parameters" and add the populate th evalues to the relevant fields and run the build.

