export KUBECONFIG=/etc/kubernetes/admin.conf

cd ../tradeInjectorFlink/target

JOB_MANAGER_NAME=$(kubectl get pods -n=mu-architecture-demo | grep jobmanager |  awk '{print $1}')
echo $JOB_MANAGER_NAME
kubectl cp mu-flink-trade-injector-0.0.1-SNAPSHOT.jar mu-architecture-demo/$JOB_MANAGER_NAME:/tmp/dummy.jar

#once copy is successful load the jar within the container
kubectl exec -it $JOB_MANAGER_NAME -n=mu-architecture-demo ./bin/flink run /tmp/mu-flink-trade-injector-0.0.1-SNAPSHOT.jar


cd ../../priceInjectorFlink/target

kubectl cp mu-flink-price-injector-0.0.1-SNAPSHOT.jar mu-architecture-demo/$JOB_MANAGER_NAME:/tmp/dummy.jar
kubectl exec -it $JOB_MANAGER_NAME -n=mu-architecture-demo ./bin/flink run /tmp/mu-flink-price-injector-0.0.1-SNAPSHOT.jar