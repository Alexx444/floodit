package ch.comem.archidep.floodit.games;

import ch.comem.archidep.floodit.games.data.CreateGameDto;
import ch.comem.archidep.floodit.games.data.CreateGameDtoBuilder;
import ch.comem.archidep.floodit.games.data.CreatedGameDto;
import ch.comem.archidep.floodit.games.data.MoveDto;
import ch.comem.archidep.floodit.games.data.PlayMoveDto;
import ch.comem.archidep.floodit.games.data.PlayMoveDtoBuilder;
import ch.comem.archidep.floodit.utils.AbstractFixtures;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.function.Consumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class GameFixtures extends AbstractFixtures {

  @Autowired
  private GameRepository gameRepository;

  public static CreatedGameDto createdGameDto() {
    return new CreatedGameDto(
      databaseId(),
      FAKER.internet().password(),
      GameState.ONGOING,
      FAKER.name().username(),
      FAKER.number().numberBetween(2, 100),
      FAKER.number().numberBetween(2, 100),
      FAKER.number().numberBetween(2, 10),
      FAKER.number().numberBetween(1, 100),
      Collections.emptyList(),
      LocalDateTime.now(),
      LocalDateTime.now()
    );
  }

  public static CreateGameDto createGameDto() {
    return createGameDto(builder -> {});
  }

  public static CreateGameDto createGameDto(
    Consumer<CreateGameDtoBuilder> build
  ) {
    var builder = createGameDtoBuilder();
    build.accept(builder);
    return builder.build();
  }

  private static CreateGameDtoBuilder createGameDtoBuilder() {
    return new CreateGameDtoBuilder(
      FAKER.name().username(),
      FAKER.number().numberBetween(2, 100),
      FAKER.number().numberBetween(2, 100),
      FAKER.number().numberBetween(2, 10),
      FAKER.number().numberBetween(1, 100)
    );
  }

  public Game game() {
    return game(builder -> {});
  }

  public Game game(Consumer<GameBuilder> build) {
    return this.gameRepository.saveAndFlush(newGame(build));
  }

  public static MoveDto moveDto() {
    return new MoveDto(
      databaseId(),
      FAKER.number().numberBetween(0, 10),
      Collections.emptySet(),
      FAKER.options().option(GameState.class),
      LocalDateTime.now()
    );
  }

  public static Game newGame(Consumer<GameBuilder> build) {
    var builder = new GameBuilder(
      createGameDtoBuilder(),
      FAKER.number().numberBetween(Long.MIN_VALUE, Long.MAX_VALUE)
    );
    build.accept(builder);
    return builder.build();
  }

  public static PlayMoveDto playMoveDto() {
    return playMoveDto(builder -> {});
  }

  public static PlayMoveDto playMoveDto(Game game) {
    return playMoveDto(builder ->
      builder
        .withGame(game)
        .withColor(FAKER.number().numberBetween(0, game.getNumberOfColors()))
    );
  }

  public static PlayMoveDto playMoveDto(Consumer<PlayMoveDtoBuilder> build) {
    var builder = new PlayMoveDtoBuilder(
      databaseId(),
      FAKER.number().numberBetween(2, 10)
    );
    build.accept(builder);
    return builder.build();
  }
}
