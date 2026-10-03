# asset-pilot-ai
An administrator creates products with a price and initial stock. An authenticated user places a single-product asset. The application validates it, locks the product row, reduces stock and saves the asset in one transaction. Repeating the same user/request key and payload returns the same asset. Users can read only their own assets. 
