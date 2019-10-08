# Evolution to Event Driven Systems

In a microservices architecture, services are command driven, which essentialy means it's synchronous. In modern systems, a particular business function could traverse across multiple microservices to complete. 

Here's an example of Command Driven Order Processing service architecture

![enter image description here](https://www.lucidchart.com/publicSegments/view/931b83d9-8093-4f97-a3a8-a01378ec887f/image.png)

Having synchronous service calls in these scenarios has several drawbacks:
- User has to wait for all the synchronous service calls to complete
- Failure in any of the synchronous service call requires rollback of transaction at several steps

***Results in less desired User experience as well as possible loss of orders due to upstream failures***

Many ecommerce enterprises are increasing their order generation rate as well as improving user experience by decoupling the upstream service calls from order acceptance call. They rely on customer information for notifications or callbacks on upstream service failures. These enterprises are achieving this by introducing Event Driven pattern to the microservices architecture.

Let's look at the same Order process flow in an Event Driven way:

![enter image description here](https://www.lucidchart.com/publicSegments/view/db0ddc4e-41a1-453b-bb51-dc8481521150/image.png)

## Let's run the Demo

### Pre-requisites

- *You have **PKS cluster** available and running on **vsphere***
-- You can follow Instructions to deploy PKS here:
-  You have Helm/Tiller deployed on the cluster
-- You can follow instructions to deploy Helm here: [https://helm.sh/docs/using_helm/#initialize-helm-and-install-tiller](https://helm.sh/docs/using_helm/#initialize-helm-and-install-tiller)
***You will have to create service account for Tiller for RBAC enabled cluster. You can follow the instructions here:*** **[https://helm.sh/docs/using_helm/#tiller-and-role-based-access-control](https://helm.sh/docs/using_helm/#tiller-and-role-based-access-control)**

***Follow this link to run this demo on PKS deployed in GCP***

### Deploy Kafka Cluster

We will first deploy Kafka Cluster. We are using Confluent Operator for PKS to deploy the Kafka cluster. It comes with 30 day evaluation license.
Confluent Kafka gives us pretty nice Control Center UI to monitor the cluster.

    $ cd kafka-helm-charts/

#### Edit kafka-helm-charts/helm/private.yaml 

You may have to edit private.yaml file to specify zones, storage info, the number of Zookeeper nodes, Kafka Brokers, TLS, external access etc

***Edit Zone info as needed***

```yaml
 ## If kubernetes is deployed in multi zone mode then specify availability-zones as appropriate
 ## If kubernetes is deployed in single availability zone then specify appropriate values
 ## For the private cloud, use kubernetes node labels as appropriate
			 zones:
			  - pks-az1
			  - pks-az2
			  - pks-az3
 ```

***Edit Storage info as needed***

```yaml
    ##  more information can be found here
    ##  https://kubernetes.io/docs/concepts/storage/storage-classes/
	 storage:
      ## Use Retain if you want to persist data after CP cluster has been uninstalled
		reclaimPolicy: Delete
		provisioner: kubernetes.io/vsphere-volume
		parameters:
		diskformat: thin
		datastore: LUN01
 ```

***Edit Zookeeper cluster info as needed***

```yaml
 ## Zookeeper cluster
 ##
zookeeper:
   name: zookeeper
   replicas: 3
   resources:
   cpu: 500m
   memory: 2Gi
 ```

***Edit Kafka Broker cluster info as needed***

Check the loadbalancer domain for the brokers. If you don't need cluster to exp
```yaml
 ## Kafka Cluster
 ##
kafka:
  name: kafka
  replicas: 5
  resources:
    cpu: 2
    memory: 4Gi
  loadBalancer:
    enabled: true
    domain: "haas-262.pez.pivotal.io"
  tls:
    enabled: false
    fullchain: |-
    privkey: |-
    cacerts: |-
  metricReporter:
    enabled: true
 ```


### Install Confluent Operator & Kafka Cluster

#### Deploy Operator
```
helm install -f ./providers/private.yaml --name operator --namespace operator --set operator.enabled=true ./confluent-operator
```

#### Patch Service Account

```
kubectl -n operator patch serviceaccount default -p '{"imagePullSecrets": [{"name": "confluent-docker-registry" }]}'  
```
#### Deploy Zookeeper
```
helm install -f ./providers/private.yaml --name zookeeper --namespace operator --set zookeeper.enabled=true ./confluent-operator  
```
#### Deploy Kafka Brokers

```
helm install -f ./providers/private.yaml --name kafka --namespace operator --set kafka.enabled=true ./confluent-operator  
```
#### Deploy Schema Registry

```
helm install -f ./providers/private.yaml --name schemaregistry --namespace operator --set schemaregistry.enabled=true ./confluent-operator  
```

#### Deploy Kafka Connect

```
helm install -f ./providers/private.yaml --name connect --namespace operator --set connect.enabled=true ./confluent-operator  
```

#### Deploy Control Center

```
helm install -f ./providers/private.yaml --name controlcenter --namespace operator --set controlcenter.enabled=true ./confluent-operator
```

#### Deploy Operator

```
helm install -f ./providers/private.yaml --name operator --namespace operator --set operator.enabled=true ./confluent-operator
```
## Deploy Apps

Change directory to to app helm charts

    $ cd app-helm-charts/

Deploy apps using helm install

    $ helm install ecommdemo/.
