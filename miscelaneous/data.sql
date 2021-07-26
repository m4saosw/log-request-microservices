-- populates database on startup
select sysdate from dual;


INSERT INTO LOGREQUEST(date, ip, request, status, user_agent) VALUES(
                                                                          '2021-07-17 00:00:03.001',
                                                                          '192.168.0.1',
                                                                          'GET / HTTP/1.1',
                                                                          200,
                                                                          'Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36');

INSERT INTO LOGREQUEST(date, ip, request, status, user_agent) VALUES(
                                                                             '2021-07-17 00:00:03.001',
                                                                             '192.168.0.1',
                                                                             'GET / HTTP/1.1',
                                                                             200,
                                                                             'Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36');

INSERT INTO LOGREQUEST(date, ip, request, status, user_agent) VALUES(
                                                                             '2021-07-11 10:00:03.001',
                                                                             '192.168.0.2',
                                                                             'GET / HTTP/1.1',
                                                                             404,
                                                                             'Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.106 Safari/537.36 OPR/38.0.2220.41');

INSERT INTO LOGREQUEST(date, ip, request, status, user_agent) VALUES(
                                                                             '2021-07-14 20:10:03.000',
                                                                             '192.168.0.1',
                                                                             'GET / HTTP/1.1',
                                                                             500,
                                                                             'Mozilla/5.0 (compatible; MSIE 9.0; Windows Phone OS 7.5; Trident/5.0; IEMobile/9.0)');

INSERT INTO LOGREQUEST(date, ip, request, status, user_agent) VALUES(
                                                                         '2020-01-01 20:10:03.000',
                                                                         '192.168.1.10',
                                                                         'GET / HTTP/1.1',
                                                                         204,
                                                                         'Mozilla/5.0 (compatible; MSIE 9.0; Windows Phone OS 7.5; Trident/5.0; IEMobile/9.0)');

