# 证书准备工作

本项目使用[cfssl][cfssl]生成CA证书及私钥，你也可以使用自己的工具来生成。

## Client

到`certs/client`目录下操作。

### 创建CA

执行下列命令生成Client CA证书和私钥：

```bash
cfssl gencert --initca=true ca-csr.json | cfssljson --bare ca
```

得到文件：

```txt
ca.csr           # CSR file
ca-key.pem       # 私钥 (PKCS #1格式，PEM编码)
ca.pem           # 证书 (X.509格式，PEM编码）
```

### 创建证书

执行下列命令生成Client证书和私钥：

```bash
cfssl gencert \
  --ca ca.pem \
  --ca-key ca-key.pem \
  --config ./cert-gen.json \
  ./cert-csr.json | cfssljson --bare ./cert
```

得到文件：

```txt
cert.csr          # CSR file
cert-key.pem      # 私钥 (PKCS #1格式，PEM编码)
cert.pem          # 证书 (X.509格式，PEM编码）
```

有些客户端只允许使用PKCS #8格式的私钥，运行下列命令得到PKCS #8私钥：

```bash
openssl pkcs8 -topk8 -in cert-key.pem -out cert-key.p8.pem
```

会提示你`Enter Encryption Password:`，你就输入`client-key123`作为密码。

### 创建Keystore

某些Java客户端需要使用PKCS #12 keystore而不是直接使用PEM文件。因此需要生成：

```bash
openssl pkcs12 -export \
  -in cert.pem \
  -inkey cert-key.pem \
  -name client \
  -CAfile ca.pem \
  -caname client-ca \
  -chain \
  -out client.keystore
```

会提示你`Enter Export Password:`，你就输入`client123`作为keystore的密码。

## Server

如果你的Server证书由知名CA签发那么可以跳过“创建CA”和”创建证书”环节。

到`certs/server`目录下操作。

### 创建CA

命令和Client创建CA一样。不过要注意`ca-csr.json`的内容不一样。

### 创建证书

命令和Client创建证书一样。不过要注意`cert-gen.json`和`cert-csr.json`的内容不一样。

生成PKCS #8私钥文件的时候，输入`server-key123`作为密码。

### 合并证书和私钥

有些Server（比如Haproxy）要求把证书和私钥合并（前后顺序无所谓）在一个文件里，用下列命令创建：

```bash
cat cert.pem cert-key.pem > cert-cert-key.pem
```

### 创建Keystore

某些Java服务端需要使用PKCS #12 keystore而不是直接使用PEM文件。因此需要生成：

```bash
openssl pkcs12 -export \
  -in cert.pem \
  -inkey cert-key.pem \
  -name server \
  -CAfile ca.pem \
  -caname server-ca \
  -chain \
  -out server.keystore
```

会提示你`Enter Export Password:`，你就输入`server123`作为keystore的密码。

## Truststore

有些Java程序使用PKCS #12的Truststore来信任CA，因此需要生成。

### Client

因为Server的证书是自签发的，Client访问Server的时候需要信任Server CA。到`certs/client`目录下执行：

```bash
keytool -importcert \
  -alias server-ca \
  -storetype pkcs12 \
  -file ../server/ca.pem \
  -noprompt \
  -keystore client.truststore
```

会提示你输入密钥库口令，你就输入`clientts123`。

### Server

Server得信任得把Client CA才能够做TLS客户端认证。到`certs/server`目录下执行：

```bash
keytool -importcert \
  -alias client-ca \
  -storetype pkcs12 \
  -file ../client/ca.pem \
  -noprompt \
  -keystore server.truststore
```

会提示你输入密钥库口令，你就输入`serverts123`。

## 文件清单

### Client

```txt
ca-key.pem        # CA私钥 (PKCS #1格式，PEM编码)
ca.pem            # CA证书 (X.509格式，PEM编码）
cert-key.pem      # 私钥 (PKCS #1格式，PEM编码)
cert-key.p8.pem   # 私钥 ，密码 client-key123 (PKCS #8格式，PEM编码)
cert.pem          # 证书 (X.509格式，PEM编码）
client.keystore   # KeyStore，密码 client123（PKCS #12格式）
client.truststore # TrustStore，密码 clientts123（PKCS #12格式）
```

### Server

```txt
ca-key.pem        # CA私钥 (PKCS #1格式，PEM编码)
ca.pem            # CA证书 (X.509格式，PEM编码）
cert-key.pem      # 私钥 (PKCS #1格式，PEM编码)
cert-key.p8.pem   # 私钥 ，密码 server-key123 (PKCS #8格式，PEM编码)
cert.pem          # 证书 (X.509格式，PEM编码）
cert-cert-key.pem # cert.pem + cert-key.pem的合并文件
server.keystore   # KeyStore，密码 server123（PKCS #12格式）
server.truststore # TrustStore，密码 serverts123（PKCS #12格式）
```



[cfssl]: https://github.com/cloudflare/cfssl