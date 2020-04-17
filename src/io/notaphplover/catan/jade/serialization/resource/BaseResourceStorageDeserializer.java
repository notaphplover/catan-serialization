package io.notaphplover.catan.jade.serialization.resource;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import io.github.notaphplover.catan.core.resource.IResourceStorage;
import io.github.notaphplover.catan.core.resource.ResourceType;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

public abstract class BaseResourceStorageDeserializer<R extends IResourceStorage>
    extends StdDeserializer<R> {

  private static final long serialVersionUID = -3548401312218917887L;

  public BaseResourceStorageDeserializer(Class<? extends R> vc) {
    super(vc);
  }

  @Override
  public R deserialize(JsonParser p, DeserializationContext ctxt)
      throws IOException, JsonProcessingException {

    return this.createResourceStorage(extractResourcesMap(p));
  }

  protected abstract R createResourceStorage(Map<ResourceType, Integer> resources);

  private static void extractResource(JsonParser p, Map<ResourceType, Integer> resources)
      throws IOException {
    switch (p.currentToken()) {
      case FIELD_NAME:
        {
          String resourceName = p.getText();
          p.nextToken();
          extractResourceValue(p, resources, resourceName);

          break;
        }
      default:
        throw new JsonParseException(p, String.format("Expected a %s token", p.currentToken()));
    }
  }

  private static Map<ResourceType, Integer> extractResourcesMap(JsonParser p) throws IOException {
    if (p.getCurrentToken() != JsonToken.START_OBJECT) {
      throw new JsonParseException(p, String.format("Expected a %s token", JsonToken.START_OBJECT));
    }

    Map<ResourceType, Integer> resources = new TreeMap<>();

    while (p.nextToken() != JsonToken.END_OBJECT) {

      extractResource(p, resources);
    }

    p.nextToken();

    return resources;
  }

  private static void extractResourceValue(
      JsonParser p, Map<ResourceType, Integer> resources, String resourceName)
      throws JsonParseException, IOException {
    switch (p.currentToken()) {
      case VALUE_NUMBER_INT:
        {
          int value = p.getIntValue();

          resources.put(ResourceType.valueOf(resourceName), value);

          break;
        }
      default:
        throw new JsonParseException(p, String.format("Expected a %s token", p.currentToken()));
    }
  }
}
