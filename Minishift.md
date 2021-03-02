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

## Build config for singlerepo with subfolders
````yaml
kind: BuildConfig
apiVersion: v1
metadata:
  name: openshift-testapp-java
spec:
  runPolicy: Serial
  source:
    git:
	  # uri: https://github.com/fabiankuhn/openshift-testapp
      # ref: feature/spring-boot # Optional define branch
  strategy:
    type: Docker
    dockerStrategy:
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


# Backup

## Build Config
```
apiVersion: build.openshift.io/v1
kind: BuildConfig
metadata:
  creationTimestamp: '2021-03-01T21:46:36Z'
  name: openshift-testapp
  namespace: testapp
  resourceVersion: '266642'
  selfLink: >-
    /apis/build.openshift.io/v1/namespaces/testapp/buildconfigs/openshift-testapp
  uid: 98b616ba-7ad7-11eb-a30f-0800279adb0f
spec:
  nodeSelector: null
  output:
    to:
      kind: ImageStreamTag
      name: 'openshift-testapp:latest'
  postCommit: {}
  resources: {}
  runPolicy: Serial
  source:
    contextDir: python
    git:
      uri: 'https://github.com/fabiankuhn/openshift-testapp'
    type: Git
  strategy:
    dockerStrategy: {}
    type: Docker
  triggers: []
status:
  lastVersion: 26
```

##  Image
```yaml
apiVersion: image.openshift.io/v1
kind: ImageStream
metadata:
  creationTimestamp: '2021-03-01T20:57:47Z'
  generation: 1
  name: openshift-testapp
  namespace: testapp
  resourceVersion: '262823'
  selfLink: >-
    /apis/image.openshift.io/v1/namespaces/testapp/imagestreams/openshift-testapp
  uid: c6e7a911-7ad0-11eb-a30f-0800279adb0f
spec:
  lookupPolicy:
    local: false
status:
  dockerImageRepository: '172.30.1.1:5000/testapp/openshift-testapp'
  tags:
    - items:
        - created: '2021-03-02T04:21:34Z'
          dockerImageReference: >-
            172.30.1.1:5000/testapp/openshift-testapp@sha256:a55335b973e1979e198570d1547d46c5672cf6c206b0d2829d7726068960d5e8
          generation: 1
          image: >-
            sha256:a55335b973e1979e198570d1547d46c5672cf6c206b0d2829d7726068960d5e8
        - created: '2021-03-02T04:11:01Z'
          dockerImageReference: >-
            172.30.1.1:5000/testapp/openshift-testapp@sha256:f4e702d6302b48fc5433a0789f420be32ecae3da3274b2b48facf8fb98f1f19c
          generation: 1
          image: >-
            sha256:f4e702d6302b48fc5433a0789f420be32ecae3da3274b2b48facf8fb98f1f19c
        - created: '2021-03-02T04:02:36Z'
          dockerImageReference: >-
            172.30.1.1:5000/testapp/openshift-testapp@sha256:6c2bc6e66ed76ac4a6d7b23e3976a0471d0ebc47615efc61eb71c91aa9dfb28c
          generation: 1
          image: >-
            sha256:6c2bc6e66ed76ac4a6d7b23e3976a0471d0ebc47615efc61eb71c91aa9dfb28c
        - created: '2021-03-02T03:01:05Z'
          dockerImageReference: >-
            172.30.1.1:5000/testapp/openshift-testapp@sha256:d8c6a6075554248c4b42859c86437b25d6f70ec68ff2cf7fda97ad6b40a969ad
          generation: 1
          image: >-
            sha256:d8c6a6075554248c4b42859c86437b25d6f70ec68ff2cf7fda97ad6b40a969ad
        - created: '2021-03-02T02:47:34Z'
          dockerImageReference: >-
            172.30.1.1:5000/testapp/openshift-testapp@sha256:1988bd480a608a16007fe60ca0b3e86d0834e642d74d339c62e5523f8e64a2cb
          generation: 1
          image: >-
            sha256:1988bd480a608a16007fe60ca0b3e86d0834e642d74d339c62e5523f8e64a2cb
        - created: '2021-03-02T02:42:47Z'
          dockerImageReference: >-
            172.30.1.1:5000/testapp/openshift-testapp@sha256:50c2e11d7f0642f80baddc560504c9d157d0bf23d2db79a0ee8a3964fd3c9152
          generation: 1
          image: >-
            sha256:50c2e11d7f0642f80baddc560504c9d157d0bf23d2db79a0ee8a3964fd3c9152
        - created: '2021-03-02T02:14:05Z'
          dockerImageReference: >-
            172.30.1.1:5000/testapp/openshift-testapp@sha256:11163145db303dc3444329e6be8dc9aeaed897e0052ea4f279274030e810c81a
          generation: 1
          image: >-
            sha256:11163145db303dc3444329e6be8dc9aeaed897e0052ea4f279274030e810c81a
        - created: '2021-03-02T01:39:23Z'
          dockerImageReference: >-
            172.30.1.1:5000/testapp/openshift-testapp@sha256:441159db95fe97c679d4e1714514fe635444f6b9aab37f0b8df71804f7f848ae
          generation: 1
          image: >-
            sha256:441159db95fe97c679d4e1714514fe635444f6b9aab37f0b8df71804f7f848ae
        - created: '2021-03-02T01:38:25Z'
          dockerImageReference: >-
            172.30.1.1:5000/testapp/openshift-testapp@sha256:32a66d3f4bc09f87b9b20c048abeaa166f34749237b89fdc2d9f27d8ec31679c
          generation: 1
          image: >-
            sha256:32a66d3f4bc09f87b9b20c048abeaa166f34749237b89fdc2d9f27d8ec31679c
        - created: '2021-03-02T00:55:33Z'
          dockerImageReference: >-
            172.30.1.1:5000/testapp/openshift-testapp@sha256:20bac7f76db649fa167308d31e42b41aa306839672b0fe304981f685d763124b
          generation: 1
          image: >-
            sha256:20bac7f76db649fa167308d31e42b41aa306839672b0fe304981f685d763124b
        - created: '2021-03-01T23:56:26Z'
          dockerImageReference: >-
            172.30.1.1:5000/testapp/openshift-testapp@sha256:8e956dd8840fc86a63c22553e226a869af607bbb42461d1102361e55f5b5df14
          generation: 1
          image: >-
            sha256:8e956dd8840fc86a63c22553e226a869af607bbb42461d1102361e55f5b5df14
        - created: '2021-03-01T22:49:56Z'
          dockerImageReference: >-
            172.30.1.1:5000/testapp/openshift-testapp@sha256:44ab1de3683736359a8a28dc9393eac36c87a192d277b3e073e062d941c57694
          generation: 1
          image: >-
            sha256:44ab1de3683736359a8a28dc9393eac36c87a192d277b3e073e062d941c57694
        - created: '2021-03-01T22:03:31Z'
          dockerImageReference: >-
            172.30.1.1:5000/testapp/openshift-testapp@sha256:d2de068e9e96a2c4662ffb2fef7b6544bd701b74cf67c922efa0848a5941b6a1
          generation: 1
          image: >-
            sha256:d2de068e9e96a2c4662ffb2fef7b6544bd701b74cf67c922efa0848a5941b6a1
        - created: '2021-03-01T22:00:38Z'
          dockerImageReference: >-
            172.30.1.1:5000/testapp/openshift-testapp@sha256:842f050ce2f264157514af9c64b682973cf5e3e0103981a6947182f24764a285
          generation: 1
          image: >-
            sha256:842f050ce2f264157514af9c64b682973cf5e3e0103981a6947182f24764a285
        - created: '2021-03-01T22:00:08Z'
          dockerImageReference: >-
            172.30.1.1:5000/testapp/openshift-testapp@sha256:d106c9017cd11dbeea1759e245a55c662788f5d7d6270a195321c13a1e083d2e
          generation: 1
          image: >-
            sha256:d106c9017cd11dbeea1759e245a55c662788f5d7d6270a195321c13a1e083d2e
        - created: '2021-03-01T21:59:38Z'
          dockerImageReference: >-
            172.30.1.1:5000/testapp/openshift-testapp@sha256:c65c2794b4ba80dbea616e4b85e88195904dfc4df461e479963319ef929e51bc
          generation: 1
          image: >-
            sha256:c65c2794b4ba80dbea616e4b85e88195904dfc4df461e479963319ef929e51bc
        - created: '2021-03-01T21:56:00Z'
          dockerImageReference: >-
            172.30.1.1:5000/testapp/openshift-testapp@sha256:08b0f645fb59844a2c37961bc542b26b2a4db6d0fae70cfff718d89c3b4fe52b
          generation: 1
          image: >-
            sha256:08b0f645fb59844a2c37961bc542b26b2a4db6d0fae70cfff718d89c3b4fe52b
        - created: '2021-03-01T21:54:28Z'
          dockerImageReference: >-
            172.30.1.1:5000/testapp/openshift-testapp@sha256:50838f75452804980e086dbd45458771715a123b223f55d0f847cb71aa282577
          generation: 1
          image: >-
            sha256:50838f75452804980e086dbd45458771715a123b223f55d0f847cb71aa282577
        - created: '2021-03-01T21:45:57Z'
          dockerImageReference: >-
            172.30.1.1:5000/testapp/openshift-testapp@sha256:38669e77a47abef9da070d428b15dda6af6dd6bc92dbd3bef3fbb61f26545647
          generation: 1
          image: >-
            sha256:38669e77a47abef9da070d428b15dda6af6dd6bc92dbd3bef3fbb61f26545647
        - created: '2021-03-01T21:14:29Z'
          dockerImageReference: >-
            172.30.1.1:5000/testapp/openshift-testapp@sha256:1e6a70ab8ab8c0599c826e44d7ad6dddccbc43877fd21f3d9698d34a7da855cc
          generation: 1
          image: >-
            sha256:1e6a70ab8ab8c0599c826e44d7ad6dddccbc43877fd21f3d9698d34a7da855cc
        - created: '2021-03-01T21:04:48Z'
          dockerImageReference: >-
            172.30.1.1:5000/testapp/openshift-testapp@sha256:463fda73bf8f774d0cc4d2e0c3fc3fc15fcb13488fa124c38f0cb7333ebaa010
          generation: 1
          image: >-
            sha256:463fda73bf8f774d0cc4d2e0c3fc3fc15fcb13488fa124c38f0cb7333ebaa010
        - created: '2021-03-01T20:58:00Z'
          dockerImageReference: >-
            172.30.1.1:5000/testapp/openshift-testapp@sha256:ec1d1d4222e2ffe04c6b8b017bf70af173f6a556c1f94d2ef8e9ccc41baeb9ae
          generation: 1
          image: >-
            sha256:ec1d1d4222e2ffe04c6b8b017bf70af173f6a556c1f94d2ef8e9ccc41baeb9ae
      tag: latest

```

## Deployment

```yaml
apiVersion: apps.openshift.io/v1
kind: DeploymentConfig
metadata:
  creationTimestamp: '2021-03-01T21:09:36Z'
  generation: 22
  name: openshift-testapp
  namespace: testapp
  resourceVersion: '263016'
  selfLink: >-
    /apis/apps.openshift.io/v1/namespaces/testapp/deploymentconfigs/openshift-testapp
  uid: 6dda68a7-7ad2-11eb-a30f-0800279adb0f
spec:
  replicas: 5
  revisionHistoryLimit: 2
  selector:
    name: openshift-testapp
  strategy:
    activeDeadlineSeconds: 21600
    resources: {}
    rollingParams:
      intervalSeconds: 1
      maxSurge: 25%
      maxUnavailable: 25%
      timeoutSeconds: 600
      updatePeriodSeconds: 1
    type: Rolling
  template:
    metadata:
      creationTimestamp: null
      labels:
        name: openshift-testapp
    spec:
      containers:
        - image: >-
            172.30.1.1:5000/testapp/openshift-testapp@sha256:a55335b973e1979e198570d1547d46c5672cf6c206b0d2829d7726068960d5e8
          imagePullPolicy: Always
          name: openshift-testapp
          ports:
            - containerPort: 8080
              protocol: TCP
          resources: {}
          terminationMessagePath: /dev/termination-log
          terminationMessagePolicy: File
      dnsPolicy: ClusterFirst
      restartPolicy: Always
      schedulerName: default-scheduler
      securityContext: {}
      terminationGracePeriodSeconds: 30
  test: false
  triggers:
    - type: ConfigChange
    - imageChangeParams:
        automatic: true
        containerNames:
          - openshift-testapp
        from:
          kind: ImageStreamTag
          name: 'openshift-testapp:latest'
          namespace: testapp
        lastTriggeredImage: >-
          172.30.1.1:5000/testapp/openshift-testapp@sha256:a55335b973e1979e198570d1547d46c5672cf6c206b0d2829d7726068960d5e8
      type: ImageChange
status:
  availableReplicas: 5
  conditions:
    - lastTransitionTime: '2021-03-01T21:09:44Z'
      lastUpdateTime: '2021-03-01T21:09:44Z'
      message: Deployment config has minimum availability.
      status: 'True'
      type: Available
    - lastTransitionTime: '2021-03-02T04:21:45Z'
      lastUpdateTime: '2021-03-02T04:21:48Z'
      message: replication controller "openshift-testapp-21" successfully rolled out
      reason: NewReplicationControllerAvailable
      status: 'True'
      type: Progressing
  details:
    causes:
      - imageTrigger:
          from:
            kind: DockerImage
            name: >-
              172.30.1.1:5000/testapp/openshift-testapp@sha256:a55335b973e1979e198570d1547d46c5672cf6c206b0d2829d7726068960d5e8
        type: ImageChange
    message: image change
  latestVersion: 21
  observedGeneration: 22
  readyReplicas: 5
  replicas: 5
  unavailableReplicas: 0
  updatedReplicas: 5
```

# Service
```yaml

apiVersion: v1
kind: Service
metadata:
  creationTimestamp: '2021-03-01T21:10:51Z'
  name: openshift-testapp
  namespace: testapp
  resourceVersion: '157146'
  selfLink: /api/v1/namespaces/testapp/services/openshift-testapp
  uid: 9a7459b5-7ad2-11eb-8757-0800279adb0f
spec:
  clusterIP: 172.30.118.135
  ports:
    - name: 8080-tcp
      port: 8080
      protocol: TCP
      targetPort: 8080
  selector:
    deploymentconfig: openshift-testapp
  sessionAffinity: None
  type: ClusterIP
status:
  loadBalancer: {}
$
```
