select * from products;
select * from shares;
select * from vector_store;

delete from vector_store;

CREATE EXTENSION vector;

SELECT column_name, data_type
FROM information_schema.columns
WHERE table_name = 'vector_store'; -- or your table name