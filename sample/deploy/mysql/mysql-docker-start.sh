docker run -p 3306:3306 -v $(dirname $(dirname `pwd`))/domain/src/migration/init:/docker-entrypoint-initdb.d -v `pwd`/data/mysql:/var/lib/mysql -e MYSQL_ROOT_PASSWORD=root -d mysql:5.7.19
