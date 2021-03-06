package io.github.notaphplover.catan.serialization.command.builder;

import io.github.notaphplover.catan.core.command.CommandType;
import io.github.notaphplover.catan.core.player.IPlayer;

public interface ICommandBuilder {

  IPlayer getDestinatary();

  CommandType getType();

  void setDestinatary(IPlayer player);

  void setType(CommandType type);
}
