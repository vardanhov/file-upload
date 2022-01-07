run   https://localhost

As Admin login admin/admin  
As User login  anton/anton,  sergey/sergey, oleg/oleg, andrey/andrey, stas/stas

To run application you should:

1.Install postgres

2.Create database "upload_file"

3.Create schema "upload"


For local starting:
1. docker-compose up --build
2. (Optional) Uncommented volume in docker-compose.yaml

Change file owner doesn't work in windows os



#Run on local with ldif
In application.properties add property spring.profiles.active=dev

or

    Choosing Run | Edit Configurations...     
    Go to the Configuration tab    
    Expand Spring Boot section Active Profiles set dev
