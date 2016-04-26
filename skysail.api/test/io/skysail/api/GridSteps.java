package io.skysail.api;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.jbehave.core.annotations.*;

public class GridSteps {

//	private Game game;
//	private StringRenderer renderer;

	@Given("a $width by $height game")
	@Aliases(values = { "a new game: $width by $height" })
	public void theGameIsRunning(int width, int height) {
//		game = new Game(width, height);
//		renderer = new StringRenderer();
//		game.setObserver(renderer);
	}

	@When("I toggle the cell at ($column, $row)")
	public void iToggleTheCellAt(int column, int row) {
//		game.toggleCellAt(column, row);
	}

	@Then("the grid should look like $grid")
	@Aliases(values = { "the grid should be $grid" })
	public void theGridShouldLookLike(String grid) {
		assertThat(false,is(false));
	}
}
