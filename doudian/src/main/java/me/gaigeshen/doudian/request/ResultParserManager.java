package me.gaigeshen.doudian.request;

import me.gaigeshen.doudian.http.ResponseContent;
import me.gaigeshen.doudian.request.result.*;
import me.gaigeshen.doudian.request.result.parser.NoSupportedResultParserException;
import me.gaigeshen.doudian.request.result.parser.ResultParser;
import me.gaigeshen.doudian.request.result.parser.ResultParserException;
import me.gaigeshen.doudian.request.result.parser.ResultParserJsonImpl;

import java.util.*;

/**
 * Result parsers manager, call parse methods to parse {@link ResponseContent} to {@link Result}
 *
 * @author gaigeshen
 */
class ResultParserManager {

  private final List<ResultParser> parsers = new ArrayList<>();

  /**
   * Create {@link ResultParserManager} with {@link ResultParser}s
   *
   * @param parsers The {@link ResultParser}s
   */
  public ResultParserManager(Collection<ResultParser> parsers) {
    if (Objects.nonNull(parsers)) {
      this.parsers.addAll(parsers);
    }
  }

  /**
   * Create {@link ResultParserManager} with {@link ResultParser}s
   *
   * @param parsers The {@link ResultParser}s
   */
  public ResultParserManager(ResultParser... parsers) {
    this(Objects.nonNull(parsers) ? Arrays.asList(parsers) : null);
  }

  /**
   * Create default result parser manager with {@link ResultParserJsonImpl}
   *
   * @return Default result parser manager
   */
  public static ResultParserManager createDefault() {
    return new ResultParserManager(new ResultParserJsonImpl());
  }

  /**
   * Do parse from {@link ResponseContent} to {@link Result}
   *
   * @param responseContent The {@link ResponseContent} cannot be null
   * @param resultClass The result class cannot be null
   * @param <R> The result type
   * @return The result obejct
   * @throws ResultParserException Could not parse
   * @throws NoSupportedResultParserException If no supported result parser
   */
  public <R extends Result> R parse(ResponseContent responseContent, Class<R> resultClass) throws ResultParserException {
    for (ResultParser parser : parsers) {
      if (parser.supports(responseContent.getType())) {
        return parser.parse(responseContent, resultClass);
      }
    }
    throw new NoSupportedResultParserException("No supported result parser for this response content type " + responseContent.getType());
  }
}
