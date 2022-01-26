# Openshift Skills
This documentation shows, how a simple java application can be deployed to openshift (minishift) via jenkins pipeline.

More Info
- Openshift Configs: [Openshift-Config](openshift)
- Jenkins Handling: [Jenkins-Config-Handling-md](_docs/Jenkins-Config-Handling.md)

## Installation
1. Setup Mac Virutalization: [https://docs.okd.io/3.11/minishift/getting-started/setting-up-virtualization-environment.html#setting-up-hyperkit-driver](https://docs.okd.io/3.11/minishift/getting-started/setting-up-virtualization-environment.html#setting-up-hyperkit-driver)
2. Install Minishift: brew cask install minishift
3. Set rights
	- `$ sudo chmod u+s /usr/local/bin/hyperkit`
	- `$ sudo chmod u+s,+x /usr/local/bin/docker-machine-driver-hyperkit`
	- `$ udo chown root:wheel /usr/local/bin/docker-machine-driver-hyperkit && sudo chmod u+s /usr/local/bin/docker-machine-driver-hyperkit`
4. Disconnect from VPN
5. Run Minishift (with Virtualbox): 
   - First run `$ minishift start --vm-driver=virtualbox` 
   - Following runs: `$ minishift start`
   
Trouble Resolve:
- minishift delete --clear-cache

## Create Github Repo
- Create Readme
- Create Jenkinsfileoc new-build --strategy docker --binary --docker-image openjdk:17-slim:openjdk:17-slim --name awesome-java-app
- See [Jenkinsfile](Jenkinsfile)

## Install Jenkins
- Open Webconsole: [https://192.168.99.101:8443/console/catalog](https://192.168.99.101:8443/console/catalog)
- Choose service catalog
- Search for Jenkins
    - Persistent (if persistent volumes are present)
    - Ephemeral (for testing)
- Add ip to `/etc/hosts` of local machine (e.g. `192.168.99.101 jenkins-testapp.192.168.99.101.nip.io`)
- Open Jenkins
- Remove sample task
- Choose `New Item`
- Choose `Multibranch Pipeline`
- Choose `Periodically run` with 1 minute interval
- Connect to Github
- See [Jenkins-Config-Handling-md](_docs/Jenkins-Config-Handling.md)

## Setup Registry and Secrets
- `$ oc create secret docker-registry docker-hub-secret --docker-server=ghcr.io --docker-username=fabiankuhn --docker-password=<my-password> --docker-email=fabian.kuhn@lemonbyte.ch`
- `$ oc secrets link default docker-hub-secret --for=pull`

## Run with Docker locally
- See [Dockerfile](backend/Dockerfile)
- `$ docker build -t ghcr.io/fabiankuhn/java-backend .`
- `$ docker run -p 8080:8080 -d --name java-backend ghcr.io/fabiankuhn/java-backend`

Check if build works

## Create Openshift Example APp
- `$ oc login` with credentials developer & developer
- `$ oc new-build --strategy docker --binary --docker-image openjdk:11-slim:openjdk:11-slim --name java-backend`
- `$ oc start-build java-backend --from-dir . --follow`
- `$ oc new-app java-backend`
- `$ oc expose svc/java-backend`
- `$ oc get route java-backend`

If deployment was successful, try out the pipeline build (which works identical). If necessary optimize the build and deploy configs.

## Result
![Demo-1](_docs/Screenshot_demo-1.png)
![Demo-2](_docs/Screenshot_demo-2.png)
![Demo-3](_docs/Screenshot_demo-3.png)
![Demo-4](_docs/Screenshot_demo-4.png)
