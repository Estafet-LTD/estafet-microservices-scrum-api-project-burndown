package com.estafet.microservices.api.project.burndown;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;

import io.opentracing.ActiveSpan;
import io.opentracing.References;
import io.opentracing.Span;
import io.opentracing.SpanContext;
import io.opentracing.Tracer;
import io.opentracing.contrib.jms.common.JmsTextMapExtractAdapter;
import io.opentracing.contrib.jms.common.JmsTextMapInjectAdapter;
import io.opentracing.contrib.jms.common.SpanJmsDecorator;
import io.opentracing.contrib.jms.common.TracingMessageUtils;
import io.opentracing.propagation.Format;
import io.opentracing.tag.Tags;

public class PropagatingTracingMessageUtils {

	 public static final String OPERATION_NAME_SEND = "jms-send";
	  public static final String OPERATION_NAME_RECEIVE = "jms-receive";
	  public static final String COMPONENT_NAME = "java-jms";

	  /**
	   * It is used by consumers only
	   */
	  static ActiveSpan buildFollowingSpan(Message message, Tracer tracer) {
	    SpanContext context = extract(message, tracer);

	    if (context != null) {

	      Tracer.SpanBuilder spanBuilder = tracer.buildSpan(OPERATION_NAME_RECEIVE)
	          //.ignoreActiveSpan()
	          .withTag(Tags.SPAN_KIND.getKey(), Tags.SPAN_KIND_CONSUMER);

	      spanBuilder.addReference(References.FOLLOWS_FROM, context);

	      ActiveSpan span = spanBuilder.startActive();

	      SpanJmsDecorator.onResponse(message, span);

	      return span;
	    }

	    return null;
	  }

	  /**
	   * Extract span context from JMS message properties or active span
	   *
	   * @param message JMS message
	   * @param tracer Tracer
	   * @return extracted span context
	   */
	  public static SpanContext extract(Message message, Tracer tracer) {
	    SpanContext spanContext = tracer
	        .extract(Format.Builtin.TEXT_MAP, new JmsTextMapExtractAdapter(message));
	    if (spanContext != null) {
	      return spanContext;
	    }

	    ActiveSpan span = tracer.activeSpan();
	    if (span != null) {
	      return span.context();
	    }
	    return null;
	  }

	  /**
	   * Inject span context to JMS message properties
	   *
	   * @param span span
	   * @param message JMS message
	   */
	  public static void inject(Span span, Message message, Tracer tracer) {
	    tracer.inject(span.context(), Format.Builtin.TEXT_MAP, new JmsTextMapInjectAdapter(message));
	  }

	  /**
	   * Build span and inject. Should be used by producers.
	   *
	   * @param message JMS message
	   * @return span
	   */
	  public static Span buildAndInjectSpan(Destination destination, final Message message,
	      Tracer tracer)
	      throws JMSException {
	    Tracer.SpanBuilder spanBuilder = tracer.buildSpan(TracingMessageUtils.OPERATION_NAME_SEND)
	        .ignoreActiveSpan()
	        .withTag(Tags.SPAN_KIND.getKey(), Tags.SPAN_KIND_PRODUCER);

	    SpanContext parent = TracingMessageUtils.extract(message, tracer);

	    if (parent != null) {
	      spanBuilder.asChildOf(parent);
	    }

	    Span span = spanBuilder.startManual();

	    SpanJmsDecorator.onRequest(destination, span);

	    TracingMessageUtils.inject(span, message, tracer);
	    return span;
	  }
	
}
