# duplicate-keys-json
As per the JSON standards specifications, it does not make any mention of duplicate keys being invalid or valid. We, at times, encounter JSON with duplicate keys for which we find it hard to parse to Java Object for further processing. 
All the available JSON libraries does not provide the way to retain the duplicated keys. 

This java based library provides simple extension to customize the behaviour of standard JSON library with duplicate keys. Henceforth, allows us to transform given JSON string to JSON Object retaining the duplicated keys as arrays.

## ILLUSTRATION

### Sample Input JSON be like :
```json
{
"Orders_Summary":{
    "TypeA":{
        "Inventory_ID":"A-1234",
        "Batch_Manufactured":"20-sep-2023"
    },
    "TypeB":{
        "Inventory_ID":"B-1234",
        "Batch_Manufactured":"10-sep-2023"
    },
    "TypeA":{
        "Inventory_ID":"A-1234",
        "Batch_Manufactured":"10-oct-2023"
    },
    "TypeA":{
        "Inventory_ID":"A-4567",
        "Batch_Manufactured":"05-oct-2023"
    },
    "TypeB":{
        "Inventory_ID":"B-3456",
        "Batch_Manufactured":"10-sep-2023"
    }
}
}
```

### JSON Object Output for above would be

```json
{
"Orders_Summary":{
    "TypeB":[
        {
            "Batch_Manufactured":"10-sep-2023",
            "Inventory_ID":"B-1234"
        },
        {
            "Batch_Manufactured":"10-sep-2023",
            "Inventory_ID":"B-3456"
        }
    ],
    "TypeA":[
        {
            "Batch_Manufactured":"20-sep-2023",
            "Inventory_ID":"A-1234"
        },
        {
            "Batch_Manufactured":"10-oct-2023",
            "Inventory_ID":"A-1234"
        },
        {
            "Batch_Manufactured":"05-oct-2023",
            "Inventory_ID":"A-4567"
        }
    ]
    }
}
```

This object can further be used to process in the application as necessary.


## Pre-Requisites

1. Maven Project
2. Java 1.8+

## USAGE

JSONObject standardizedJson = new CustomizedJsonObject(new CustomizedJsonTokener("YOUR_JSON_STRING"));
