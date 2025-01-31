# safe-stream-mapper
safe-stream-mapper is tiny yet powerful lib to gracefully handle exceptions in map() function in a stream. 
It will map the items which are successfully mapped and wrap them into success type. 
Item which have exceptions while mapping will return null for success object and wrap exception itself in the fail object
