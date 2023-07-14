# streammapper
streeammapper is tiny yet powerfull lib to gracfully handle exceptions in map() fuction in a stream. 
It will map the items which are succsessfully mapped and wrap them into success type. 
Item which have exceptions while mapping will retun null for success object and wrap exception itself in the fail object
