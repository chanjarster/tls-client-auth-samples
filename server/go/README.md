# Client - Go HttpServer

```go
func main() {

	certPool := x509.NewCertPool()
	clientCa, err := ioutil.ReadFile("../../certs/client/ca.pem")
	if err != nil {
		panic(err)
	}
	if !certPool.AppendCertsFromPEM(clientCa) {
		panic("load client ca.pem failed")
	}

	tlsConfig := &tls.Config{
		ClientAuth:   tls.RequireAndVerifyClientCert,
		ClientCAs:    certPool,
	}

	var handlerFunc http.HandlerFunc = func(rw http.ResponseWriter, req *http.Request) {
		rw.Write([]byte("Hello world"))
	}

	server := &http.Server{
		Addr:      ":8443",
		Handler:   handlerFunc,
		TLSConfig: tlsConfig,
	}

	panic(server.ListenAndServeTLS("../../certs/server/cert.pem","../../certs/server/cert-key.pem"))

}
```





