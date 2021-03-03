# Jenkins
Steps to set up a pipeline and for faster development

## Handling
For faster development choose replay on the deploy. This will directly relaunch the jenkins build.
![Screenshot_Jenkins](Screenshot_Jenkins-1.png)

## Config Pipeline
Follow steps to set up multibranch pipeline

1. Add new Item
![Screenshot_Jenkins](Screenshot_Jenkins-3.png)
   
2. Select Multibranch pipeline
![Screenshot_Jenkins](Screenshot_Jenkins-6.png)
   
3. Setup Git connection
![Screenshot_Jenkins](Screenshot_Jenkins-5.png)

4. Setup JDK
![Screenshot_Jenkins_2](Screenshot_Jenkins-2.png)

In the pipeline:
```groovy
tools {
    jdk "jdk-11.0.1"
}
```

5. Result
![Screenshot_Jenkins](Screenshot_Jenkins-7.png)
