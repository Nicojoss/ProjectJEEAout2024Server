package be.jossart.javabeans;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import be.jossart.dao.AbstractDAOFactory_Server;
import be.jossart.dao.DAO_Server;
import be.jossart.dao.RecipeStepDAO_Server;

public class RecipeStep_Server implements Serializable{
	//ATTRIBUTES
	private static final long serialVersionUID = -1368662956594765085L;
	private int idRecipeStep;
	private String instruction;
	private Recipe_Server recipe;
	private static final AbstractDAOFactory_Server adf = AbstractDAOFactory_Server.getFactory();
	private static final DAO_Server<RecipeStep_Server> recipeStepDAO = adf.getRecipeStepDAO();
	//CTOR
	public RecipeStep_Server() { 
	}
	public RecipeStep_Server(int idRecipeStep, String instruction, Recipe_Server recipe) {
		this.idRecipeStep = idRecipeStep;
		this.instruction = instruction;
		this.recipe = recipe;
	}
	public RecipeStep_Server(String instruction, Recipe_Server recipe) {
		this.instruction = instruction;
		this.recipe = recipe;
	}
	//METHODS
	public boolean create() {
		return recipeStepDAO.create(this);
	}
	public boolean delete() {
		return recipeStepDAO.delete(this);
	}
	public boolean update() {
		return recipeStepDAO.update(this);
	}
	public static RecipeStep_Server find(int id) {
		RecipeStepDAO_Server dao = (RecipeStepDAO_Server) adf.getRecipeStepDAO();
		return dao.find(id);
	}
	public static RecipeStep_Server findId(RecipeStep_Server recipeStep) {
		RecipeStepDAO_Server dao = (RecipeStepDAO_Server) adf.getRecipeStepDAO();
		return dao.findId(recipeStep);
	}
	public static List<Integer> findIds(int id) {
		RecipeStepDAO_Server dao = (RecipeStepDAO_Server) adf.getRecipeStepDAO();
		return dao.findIds(id);
	}
	//GETTERS SETTERS
	public int getIdRecipeStep() {
		return idRecipeStep;
	}
	public void setIdRecipeStep(int idRecipeStep) {
		this.idRecipeStep = idRecipeStep;
	}
	public String getInstruction() {
		return instruction;
	}
	public void setInstruction(String instruction) {
		this.instruction = instruction;
	}
	public Recipe_Server getRecipe() {
		return recipe;
	}
	public void setRecipe(Recipe_Server recipe) {
		this.recipe = recipe;
	}
	@Override
	public String toString() {
		return "RecipeStep_Server [idRecipeStep=" + idRecipeStep + ", instruction=" + instruction + ", recipe=" + recipe
				+ "]";
	}
	@Override
	public int hashCode() {
		return Objects.hash(idRecipeStep, instruction, recipe);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RecipeStep_Server other = (RecipeStep_Server) obj;
		return idRecipeStep == other.idRecipeStep && Objects.equals(instruction, other.instruction)
				&& Objects.equals(recipe, other.recipe);
	}
}
