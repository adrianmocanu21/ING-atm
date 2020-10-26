# ING-atm

The project is written in java 11, using the 5.2.9 Spring version and a 4.2.10 mongodb version
Application runs on port 8080

1.Endpoints: 

a.POST http://localhost:8080/internal-api/currency
   Body example: 
{
  "30": 2,
  "1000" : 14,
  "100": 1,
  "40":700
}


b.GET http://localhost:8080/api/currency/{amount}
   Response example: 
   
{
    "100": 12,
    "40": 1,
    "30": 1
}
