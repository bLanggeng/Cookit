package de.htw.mada.cookit.utils;

public interface Constant {
	
		String IP_ADDRESS = "http://192.168.2.8";
	
	    short SUCCESS = 1;
	    short MYSQL_CONNECT_ERROR = 0;
	
	    String ERROR = "ERROR";
	
	    String ID = "id";
	    String IMAGE_PATH = "image path";
	    String DISH_NAME = "dish name";
	    int	EMPTY_ID = 0;
	    short GET_ALL_RECIPEES = 0;
	    short INSERT_RECIPE = 1;
	    short GET_A_RECIPE = 2;
	    short UPLOAD_PICTURE = 3;
	    
	    String DELIMITER_DATA = ";";
	    String DELIMITER_RC = "=";
	    String DELIMITER_RECIPE = "#";
	    String NO_COMMENT = "no comment";
	
	    String TAB_HOME = "HOME";
	    String TAB_FAVORITE = "FAVORITE";
	    
	    String EMPTY_LIST = "List is empty";
	    String SEARCH_NOT_FOUND = "search not found";
	
	    String NO_CONNECTION = "No connection to database";
	    String FILL_NAME = "Name must be filled";
	    String FILL_INGREDIENTS = "Ingredients must be filled";
	    String FILL_DIRECTIONS = "Directions must be filled";
	
	    String UPLOAD_SUCCESS = "Recipe has been added to database";
	    String DEFAULT_AUTHOR = "Anonymous";
	
	    String FROM_FAVORITE = "from favorite";
	    String TO_FAVOURITE_LIST = "to Favourite list";
	
	    String MESSAGE_ADD_FAVORITE = "A recipe has been added to favorite list";
	    String MESSAGE_REMOVE_FAVORITE = "A recipe has been removed from favorite list";
	
	    int REQUEST_CAMERA = 0;
	    int SELECT_FILE = 1;
	    String TAKE_PHOTO = "Take photo";
	    String FROM_GALLERY = "Choose from gallery";
	    String CANCEL = "Cancel";
	    String ADD_PHOTO = "Add photo !";
}
