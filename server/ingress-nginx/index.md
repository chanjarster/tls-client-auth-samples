# Server - Ingress Nginx

本文介绍在K8S的Ingress Nginx中部署TLS Client Auth。

### 配置TLS Server Auth

首先你得给你的Ingress配置一个TLS Server Auth，在你可以使用[cert-manager][cert-manager]来帮你自动申请证书，具体安装方法请自行看文档[quick start][cert-manager-quick-start]。

本文和[quick start][cert-manager-quick-start]一样，建两个Cluster Issuer，这两个Cluster Issuer创建成功后可以被整个K8S集群使用：

staging-issuer.yaml：

```yaml
apiVersion: certmanager.k8s.io/v1alpha1
kind: ClusterIssuer
metadata:
  name: letsencrypt-staging
spec:
  acme:
    # The ACME server URL
    server: https://acme-staging-v02.api.letsencrypt.org/directory
    # Email address used for ACME registration
    email: your@examle.com
    # Name of a secret used to store the ACME account private key
    privateKeySecretRef:
      name: letsencrypt-staging
    # Enable the HTTP-01 challenge provider
    solvers:
    - http01:
        ingress:
          class:  nginx
```

prod-issuer.yaml：

```yaml
apiVersion: certmanager.k8s.io/v1alpha1
kind: ClusterIssuer
metadata:
  name: letsencrypt-prod
spec:
  acme:
    # The ACME server URL
    server: https://acme-v02.api.letsencrypt.org/directory
    # Email address used for ACME registration
    email: your@examle.com
    # Name of a secret used to store the ACME account private key
    privateKeySecretRef:
      name: letsencrypt-prod
    # Enable the HTTP-01 challenge provider
    solvers:
    - http01:
        ingress:
          class: nginx
```

查看一下是否创建成功：

```bash
kubectl describe clusterissuer letsencrypt-staging
kubectl describe clusterissuer letsencrypt-prod
```

然后在你的Ingress中添加`certmanager.k8s.io/cluster-issuer` annotation，就可以开启TLS Server Auth，先用`letsencrypt-staging`试试（prod有[配额限制][letsencrypt-rate-limit]），kuard-ingress.yaml：

```yaml
apiVersion: extensions/v1beta1
kind: Ingress
metadata:
  name: kuard
  annotations:
    certmanager.k8s.io/cluster-issuer: "letsencrypt-staging"
    nginx.ingress.kubernetes.io/ssl-redirect: "true"
spec:
  tls:
  - hosts:
    - example.example.com
    secretName: quickstart-example-tls
  rules:
  - host: example.example.com
    http:
      paths:
      - path: /
        backend:
          serviceName: kuard
          servicePort: 80
```

看看是否成功：

```bash
kubectl describe ing kuard
```

如果成功了，那么就把issuer改成`letsencrypt-prod`。

### 配置TLS Client Auth

参考[Ingress Nginx - Client Certificate Authentication Example][ing-client-certs-example]里的做法，把你信任的CA证书添加到K8S Secret中：

```bash
kubectl create secret generic ca-secret --from-file=ca.crt=ca.pem
```

注意Secret中的key必须得是`ca.crt`，看看是否创建成功：

```bash
kubectl describe secret ca-secret
```

在你的Ingress中添加几个`nginx.ingress.kubernetes.io/auth-tls-*` annotation：

```yaml
apiVersion: extensions/v1beta1
kind: Ingress
metadata:
  name: kuard
  annotations:
    certmanager.k8s.io/cluster-issuer: "letsencrypt-staging"
    nginx.ingress.kubernetes.io/ssl-redirect: "true"
    nginx.ingress.kubernetes.io/auth-tls-verify-client: "on"
    nginx.ingress.kubernetes.io/auth-tls-secret: "<namespace>/ca-secret"
    nginx.ingress.kubernetes.io/auth-tls-verify-depth: "1"
spec:
  tls:
  - hosts:
    - example.example.com
    secretName: quickstart-example-tls
  rules:
  - host: example.example.com
    http:
      paths:
      - path: /
        backend:
          serviceName: kuard
          servicePort: 80
```

注意`nginx.ingress.kubernetes.io/auth-tls-secret`这个annotation的格式是`<namespace>/<secret-name>`，不要填错了。

注解的参考：[Ingress Nginx - Client Certificate Authentication][ing-client-auth]



[cert-manager]: https://docs.cert-manager.io
[cert-manager-quick-start]: https://docs.cert-manager.io/en/latest/tutorials/acme/quick-start/index.html
[ing-client-certs-example]: https://kubernetes.github.io/ingress-nginx/examples/auth/client-certs/
[ing-client-auth]: https://kubernetes.github.io/ingress-nginx/user-guide/nginx-configuration/annotations/#client-certificate-authentication
[letsencrypt-rate-limit]: https://letsencrypt.org/docs/rate-limits/

