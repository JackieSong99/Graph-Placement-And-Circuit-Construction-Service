FROM openjdk:slim
COPY --from=python:3.9 / /
ENV PYTHONDONTWRITEBYTECODE=1
ENV PYTHONUNBUFFERED=1

WORKDIR /app
COPY requirements.txt /app/requirements.txt
# RUN apt-get update
# RUN apt-get -y install libgeos-dev
# RUN apt-get update

# RUN apt-get upgrade
RUN sed -i 's#http://archive.ubuntu.com/#http://mirrors.tuna.tsinghua.edu.cn/#' /etc/apt/sources.list;
# RUN apt-get update --fix-missing
RUN apt-get update --fix-missing && apt-get install -y minisat --fix-missing
# RUN apt-get install -y minisat
RUN pip install --upgrade pip
RUN pip install -r requirements.txt
COPY . /app
WORKDIR /app/optimization_service/LFV0426
RUN apt-get update -y
RUN apt-get install -y libcommons-beanutils-java
RUN apt-get install -y libcommons-collections3-java
RUN apt-get install -y libcommons-lang3-java
RUN apt-get install libcommons-logging-java
RUN apt-get install libezmorph-java
RUN apt-get install -y libjenkins-json-java
CMD javac Run.java
WORKDIR /app
CMD python manage.py makemigrations && python manage.py migrate && python manage.py test && python manage.py runserver 0.0.0.0:8999

