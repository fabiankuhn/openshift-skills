# Minishift

## Installation
1. Setup Mac Virutalization: [https://docs.okd.io/3.11/minishift/getting-started/setting-up-virtualization-environment.html#setting-up-hyperkit-driver(https://docs.okd.io/3.11/minishift/getting-started/setting-up-virtualization-environment.html#setting-up-hyperkit-driver)
2. Install Minishift: brew cask install minishift
3. Set rights
	- `$ sudo chmod u+s /usr/local/bin/hyperkit`
	- `$ sudo chmod u+s,+x /usr/local/bin/docker-machine-driver-hyperkit`
	- `$ udo chown root:wheel /usr/local/bin/docker-machine-driver-hyperkit && sudo chmod u+s /usr/local/bin/docker-machine-driver-hyperkit`
4. Disconnect from VPN
5. Run Minishift: `$ minishift run`


Touble Resolve:
- minishift delete --clear-cache


## Create Github Repo
- Create Readme
- Create Jenkinsfile

```groovy
pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                echo 'Building..'
            }
        }
        stage('Test') {
            steps {
                echo 'Testing..'
            }
        }
        stage('Deploy') {
            steps {
                echo 'Deploying....'
            }
        }
    }
}
```


## Install Jenkins
- Open Webconsole: [https://192.168.99.101:8443/console/catalog](https://192.168.99.101:8443/console/catalog)
- Choose servce catalog
- Search for Jenkins
	- Persistent (if persisten volumes are present)
	- Ephemeral (for testing)
- Add ip to /etc/hosts (e.g. `192.168.99.101 jenkins-testapp.192.168.99.101.nip.io`)
- Open Jenkins
- Remove sample task
- Choose `New Item`
- Choose `Multibranch Pipeline`
- Choose `Periodically run` with 1 minute interval
- Connect to Github

(Screenshots)


## Deploy Docker Image

### Create ImageStream
```yaml
apiVersion: image.openshift.io/v1
kind: ImageStream
metadata:
  name: simple-webapp-docker
```

### Create Build from Docker
```yaml
kind: BuildConfig
apiVersion: v1
metadata:
  name: simple-webapp-docker
spec:
  runPolicy: Serial
  source:
    git:
      uri: https://github.com/mmumshad/simple-webapp-docker
  strategy:
    type: Docker
    dockerStrategy:
  output:
    to:
      kind: ImageStreamTag
      name: simple-webapp-docker:latest
  ```

### Add Deployment Config
```yaml
kind: "DeploymentConfig"
apiVersion: "v1"
metadata:
  name: "simple-webapp-docker"
spec:
  template: 
    metadata:
      labels:
        name: "simple-webapp-docker"
    spec:
      containers:
        - name: "simple-webapp-docker"
          image: "simple-webapp-docker:latest"
          ports:
            - containerPort: 8080
              protocol: "TCP"
  replicas: 5 
  triggers:
    - type: "ConfigChange" 
    - type: "ImageChange" 
      imageChangeParams:
        automatic: true
        containerNames:
          - "simple-webapp-docker"
        from:
          kind: "ImageStreamTag"
          name: "simple-webapp-docker:latest"
  strategy: 
    type: "Rolling"
  paused: false 
  revisionHistoryLimit: 2 
  minReadySeconds: 0 
  ```

### Create a service and route
- Add Yaml for service
```yaml
apiVersion: v1
kind: Service
metadata:
  name: simple-webapp-docker    
spec:
  selector:                  
    deploymentconfig: simple-webapp-docker
  ports:
  - name: 8080-tcp
    port: 8080               
    protocol: TCP
    targetPort: 8080
```
- Click on `add route` and leave all to default
