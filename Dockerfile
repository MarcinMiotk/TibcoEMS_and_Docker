FROM tutum/centos
RUN yum update -y && yum install -y java
ENV JAVA_HOME="/usr/lib/jvm/jre-openjdk/"
COPY TIB_ems-dev_8.3.0_linux_x86.zip /tmp/
RUN yum install java
RUN useradd -ms /bin/bash tibco
ENV HOME /home/tibco
RUN yum -y install unzip
RUN mkdir /tmp/software/
RUN mv /tmp/TIB_ems-dev_8.3.0_linux_x86.zip /tmp/software/
RUN unzip /tmp/software/TIB_ems-dev_8.3.0_linux_x86.zip -d /tmp/software/
RUN /tmp/software/TIBCOUniversalInstaller-lnx-x86.bin -silent
EXPOSE 7222
COPY ems.sh /opt/tibco/ems/
RUN chmod +x /opt/tibco/ems/ems.sh
RUN chown -R tibco:tibco /tmp/software/
RUN chown -R tibco:tibco /opt/tibco/
RUN chown -R tibco:tibco /home/user/tibco/
RUN sed -i '/track_message_ids       =/c\track_message_ids       = enabled' /home/user/tibco/tibco/cfgmgmt/ems/data/tibemsd.conf
RUN sed -i '/track_correlation_ids   =/c\track_correlation_ids   = enabled' /home/user/tibco/tibco/cfgmgmt/ems/data/tibemsd.conf
USER tibco
CMD /opt/tibco/ems/ems.sh
