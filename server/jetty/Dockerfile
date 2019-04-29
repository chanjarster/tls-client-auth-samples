FROM jetty:9

RUN java -jar "$JETTY_HOME/start.jar" --add-to-startd=ssl,https
COPY ssl.ini /var/lib/jetty/start.d/ssl.ini