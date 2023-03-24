drop user c##product;

--계정 생성
create user c##product identified by product1234
    default tablespace users
    temporary tablespace temp
    profile default;

--권한 부여
grant connect, resource to c##product;
grant create view, create synonym to c##product;
grant unlimited tablespace to c##product;

--락 풀기
alter user c##product account unlock;