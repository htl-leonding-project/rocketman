./mvnw clean package -Dmaven.test.skip=true
docker build -f src/main/docker/Dockerfile.jvm -t rocketman/mqtt-backend .

# normal run
# docker run -i --rm -p 8080:8080 --add-host host.docker.internal:host-gateway rocketman/mqtt-backend
# interactive shell in image
# docker run -it rocketman/mqtt-backend sh