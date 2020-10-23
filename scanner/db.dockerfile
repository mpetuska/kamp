FROM mongo AS builder
ARG input=out
COPY $input /home/data
RUN find /home/data -type f ! -name '_.json' -delete

FROM mongo
COPY --from=builder /home/data /home/data
CMD for file in /home/data/*; do echo "Importing $file" && mongoimport --drop --host mongo --db main --collection ${file##*/} --file $file/_.json; done
