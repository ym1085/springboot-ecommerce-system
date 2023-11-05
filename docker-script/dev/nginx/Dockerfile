FROM nginx:1.23.2
WORKDIR /etc/nginx

RUN apt-get update && apt-get install -y --no-install-recommends \
    logrotate \
    cron \
    vim \
    net-tools \
    telnet \
    procps \
    iputils-ping \
    dos2unix

COPY nginx.conf nginx.conf
COPY wait-for-it.sh wait-for-it.sh
COPY nginx /etc/logrotate.d/nginx

RUN dos2unix /etc/logrotate.d/nginx /etc/nginx/wait-for-it.sh \
    && apt-get --purge remove -y dos2unix \
    && apt-get autoremove -y

RUN chmod +x wait-for-it.sh
RUN chmod 755 /var/log/nginx

RUN echo "1 2,12 * * * root /usr/sbin/logrotate /etc/logrotate.conf" >> /etc/crontab

HEALTHCHECK --interval=10s --timeout=10s CMD curl --location --request GET 'http://127.0.0.1:80/health-check' || exit 1
CMD ["sh", "-c", "cron && ./wait-for-it.sh shopping-mall:8081 -- nginx -g 'daemon off;'"]