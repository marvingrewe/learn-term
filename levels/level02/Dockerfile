FROM alpine:3.14.0
WORKDIR /app
RUN apk add --no-cache bash ncurses git
COPY /level02/goal /app
COPY /level02/task /app
ADD /verify /usr/bin
ADD /showtask /usr/bin
RUN chmod ugo+x /usr/bin/verify /usr/bin/showtask
RUN adduser -D -u 1000 user
RUN mkdir -p /home/user
RUN chown user:user /app -R
WORKDIR /
ENV WORKDIR=/app
USER user
RUN git config --global user.email "test@test.com"
RUN git config --global user.name "Test Testington"
CMD ["/bin/bash"]
