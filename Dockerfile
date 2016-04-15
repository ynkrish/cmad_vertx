FROM vertx/vertx3

#Environment variables
ENV VERTICLE_NAME com.krish.cmad.RestResponderVerticle
ENV VERTICLE_FILE target/

ENV VERTICLE_HOME /usr/verticles

#Expose port of the docker image
EXPOSE 8080

#Copy your verticle to the container
COPY $VERTICLE_FILE $VERTICLE_HOME/

#Launch the verticle
WORKDIR $VERTICLE_HOME
ENTRYPOINT ["sh", "-c"]
CMD ["vertx run $VERTICLE_NAME -cp $VERTICLE_HOME/*"]
