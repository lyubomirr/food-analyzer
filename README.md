# Food Analyzer
A simple client-server applications that analyzes different kinds of food using National Agricultural Library's rest api.
The server support the following commands:


`get-food <food_name>` - the server fetches data from the api and returns a response containing fdcId, description, and gtinUPC code.

`get-food-report <food_fdcId>` - server returns product name, ingredients and nutrient information by foodId.

`get-food-by-barcode --code=<gtinUpc_code>|--img=<barcode_image_file>` - server returns information for a product by its barcode **if there is cached information about it**
by providing the barcode itself or a path to a picture of it. If both arguments are provided `img` is ignored.

### Example commands:
```
get-food beef noodle soup
get-food-report 415269
get-food-by-barcode --img=D:\Photos\BarcodeImage.jpg --code=009800146130
```
