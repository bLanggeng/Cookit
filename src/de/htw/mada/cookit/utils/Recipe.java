package de.htw.mada.cookit.utils;

import android.graphics.Bitmap;

public class Recipe {

	private String dishName, author, comment, type, difficulty, duration, ingredients, directions, notes;
	int id;
	Bitmap image;
	
	public Recipe() {
		this.comment = "";
		this.duration = "";
		this.notes = "";
	}

	public Recipe(int id,String dn, String a, String c, String t, String dif, String dur, String i, String dir, String n, Bitmap bm) {
		this.id = id;
		this.dishName = dn;
		this.author = a;
		this.comment = c;
		this.type = t;
		this.difficulty = dif;
		this.duration = dur;
		this.ingredients = i;
		this.directions = dir;
		this.notes = n;
		this.image = bm;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public void setDishName(String dn) {
		this.dishName = dn;
	}

	public void setAuthor(String a) {
		this.author = a;
	}
	
	public void setComment(String c) {
		this.comment = c;
	}

	public void setType(String t) {
		this.type = t;
	}

	public void setDifficulty(String dif) {
		this.difficulty = dif;
	}
	
	public void setDuration(String dur) {
		this.duration = dur;
	}

	public void setIngredients(String ing) {
		this.ingredients = ing;
	}
	
	public void setDirections(String dir) {
		this.directions = dir;
	}

	public void setNotes(String n) {
		this.notes = n;
	}

	public int getId() {
		return id;
	}
	
	public String getDishName() {
		return dishName;
	}

	public String getAuthor() {
		return author;
	}

	public String getComment() {
		return comment;
	}
	
	public String getType() {
		return type;
	}

	public String getDif() {
		return difficulty;
	}
	
	public String getDur() {
		return duration;
	}
	
	public String getIng() {
		return ingredients;
	}
	
	public String getDir() {
		return directions;
	}
	
	public String getNotes() {
		return notes;
	}
	
	public Bitmap getImage() {
		return image;
	}
	
	@Override
	public String toString() {
		String result = "" + id + dishName + author + type + comment + image;
		return result;
	}
	
}
