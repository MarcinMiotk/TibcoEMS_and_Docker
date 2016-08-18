# TibcoEMS_and_Docker
Tibco EMS and Docker integration

# Steps

1) Download 90-day trial version of Tibco EMS from https://tap.tibco.com/storefront/trialware/tibco-enterprise-message-service/prod15032.html 

2) The name of the file should be TIB_ems-dev_8.3.0_linux_x86.zip and save this file in the directory where the Dockerfile is.

3.a.1) If you want to have your own image, type:

        docker build -t mami/tibco .

  where mami/tibco is your own name.

3.a.2) Log in to the DockerHub (on your account) and push this image to your account:
      
      docker login --username=mami --email=mami@snmill.com
      docker push mami/tibco


4) On your another docker-machine you can run

      docker run -it -p 7222:7222 -p 7224:7224 -e PUBLIC_IP=yourip.snmill.com mami/tibco
      
  After that you can connect with client by tcp://192.168.99.100:8222 (where IP is your's docker machine)



The logs in the Tibco container are located at:

        /home/user/tibco/tibco/cfgmgmt/ems/data/datastore/



		
		
		
*) To manage remotely the Docker Machine in Linux, you have to run
		sudo dockerd -D --tls=false -H tcp://yourip:2376
		
	