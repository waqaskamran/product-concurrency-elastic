Install elastic search 
Create following index 
PUT productindex

craet following document 


POST productindex/_doc
{
"name" : "perfume11",
"price" : "21",
"category":"",
"counter" : 0
}

Search by Query where Category is Null 

GET productindex/_search
{
"query": {"terms": {"category.keyword": [""]}}

}
