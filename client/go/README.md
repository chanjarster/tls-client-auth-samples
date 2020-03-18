# Client - Go HttpClient

## Server证书不是权威CA签发，请求时不做TLS服务端认证

```go
func unknownCaDisableServerAuth() {

	cert, err := tls.LoadX509KeyPair("../../certs/client/cert.pem", "../../certs/client/cert-key.pem")
	if err != nil {
		panic(err)
	}

	tp := http.DefaultTransport.(*http.Transport).Clone()
	tp.TLSClientConfig = &tls.Config{
		Certificates:       []tls.Certificate{cert},
		InsecureSkipVerify: true,
	}

	c := http.Client{
		Transport: tp,
	}

	resp, err := c.Get("https://localhost:8443")
	if err != nil {
		panic(err)
	}
	resp.Write(os.Stdout)

}
```

## Server证书不是权威CA签发，请求时做TLS服务端认证

```go
func unknownCaEnableServerAuth() {

	cert, err := tls.LoadX509KeyPair("../../certs/client/cert.pem", "../../certs/client/cert-key.pem")
	if err != nil {
		panic(err)
	}

	certPool := x509.NewCertPool()
	serverCa, err := ioutil.ReadFile("../../certs/server/ca.pem")
	if err != nil {
		panic(err)
	}
	if !certPool.AppendCertsFromPEM(serverCa) {
		panic("load server ca.pem failed")
	}

	tp := http.DefaultTransport.(*http.Transport).Clone()

	tp.TLSClientConfig = &tls.Config{
		Certificates: []tls.Certificate{cert},
		RootCAs:      certPool,
	}

	c := http.Client{
		Transport: tp,
	}

	resp, err := c.Get("https://localhost:8443")
	if err != nil {
		panic(err)
	}
	resp.Write(os.Stdout)

}
```

## 如果Server证书是权威CA签发

```go
func knownCa() {
  
	cert, err := tls.LoadX509KeyPair("../../certs/client/cert.pem", "../../certs/client/cert-key.pem")
	if err != nil {
		panic(err)
	}

	tp := http.DefaultTransport.(*http.Transport).Clone()

	tp.TLSClientConfig = &tls.Config{
		Certificates: []tls.Certificate{cert},
	}

	c := http.Client{
		Transport: tp,
	}

	resp, err := c.Get("https://baidu.com")
	if err != nil {
		panic(err)
	}
	resp.Write(os.Stdout)
}
```





