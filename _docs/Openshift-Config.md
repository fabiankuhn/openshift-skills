# Openshift Configs
These configs are used to create a app from a docker Repo

### Create ImageStream
```yaml
apiVersion: image.openshift.io/v1
kind: ImageStream
metadata:
  name: simple-webapp-docker
```

### Create Build from Docker (OLD)
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

## Build config
````yaml
kind: BuildConfig
apiVersion: v1
metadata:
  name: openshift-testapp-java
spec:
  runPolicy: Serial
  source:
    binary: {}
    type: Binary
  strategy:
    dockerStrategy:
      from:
        kind: ImageStreamTag
        name: 'openjdk:11-slim'
  output:
    to:
      kind: ImageStreamTag
      name: openshift-testapp-java:latest
````

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
    - type: "ImageChange"  # TODO to separate deployment build step, change this config
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
