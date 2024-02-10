package com.bigbluebubble.monitoring;

import com.amazonaws.services.cloudwatch.AmazonCloudWatchClient;
import com.amazonaws.services.cloudwatch.model.Dimension;
import com.amazonaws.services.cloudwatch.model.MetricDatum;
import com.amazonaws.services.cloudwatch.model.PutMetricDataRequest;
import com.amazonaws.services.cloudwatch.model.StandardUnit;
import com.bigbluebubble.BBBServer.util.SimpleLogger;
import com.smartfoxserver.bitswarm.controllers.IController;
import com.smartfoxserver.bitswarm.controllers.IControllerManager;
import com.smartfoxserver.bitswarm.core.BitSwarmEngine;
import com.smartfoxserver.v2.SmartFoxServer;
import com.smartfoxserver.v2.extensions.SFSExtension;
import java.util.ArrayList;
import java.util.Collection;

public class CloudWatchReporter implements Runnable {
   private AmazonCloudWatchClient cloudwatch = new AmazonCloudWatchClient();
   private Collection<MetricDatum> data = new ArrayList();
   private Collection<Dimension> dimensions = new ArrayList();

   public CloudWatchReporter(SFSExtension e) {
      Dimension d = new Dimension();
      d.setName("InstanceId");
      d.setValue(EC2Metadata.getEC2InstanceId());
      this.dimensions.add(d);
      SimpleLogger.trace("cloudwatch = " + this.cloudwatch);
   }

   public void reportUserCount() {
      MetricDatum o = new MetricDatum();
      o.setMetricName("UserCount");
      o.setUnit(StandardUnit.Count);
      o.setValue((double)SmartFoxServer.getInstance().getUserManager().getUserCount());
      o.setDimensions(this.dimensions);
      this.data.add(o);
   }

   public void reportMemory() {
      double mb = 1048576.0D;
      Runtime runtime = Runtime.getRuntime();
      MetricDatum o = new MetricDatum();
      o.setMetricName("UsedMemory");
      o.setUnit(StandardUnit.Megabytes);
      o.setValue((double)(runtime.totalMemory() - runtime.freeMemory()) / 1048576.0D);
      o.setDimensions(this.dimensions);
      this.data.add(o);
      o = new MetricDatum();
      o.setMetricName("FreeMemory");
      o.setUnit(StandardUnit.Megabytes);
      o.setValue((double)runtime.freeMemory() / 1048576.0D);
      o.setDimensions(this.dimensions);
      this.data.add(o);
      o = new MetricDatum();
      o.setMetricName("TotalMemory");
      o.setUnit(StandardUnit.Megabytes);
      o.setValue((double)runtime.totalMemory() / 1048576.0D);
      o.setDimensions(this.dimensions);
      this.data.add(o);
      o = new MetricDatum();
      o.setMetricName("MaxMemory");
      o.setUnit(StandardUnit.Megabytes);
      o.setValue((double)runtime.maxMemory() / 1048576.0D);
      o.setDimensions(this.dimensions);
      this.data.add(o);
   }

   public void reportThreads() {
      MetricDatum o = new MetricDatum();
      o.setMetricName("OutgoingMessageQueueSize");
      o.setUnit(StandardUnit.Count);
      o.setValue((double)BitSwarmEngine.getInstance().getSocketWriter().getQueueSize());
      o.setDimensions(this.dimensions);
      this.data.add(o);
      IControllerManager cmg = BitSwarmEngine.getInstance().getControllerManager();
      IController extCtrl = cmg.getControllerById((byte)1);
      o = new MetricDatum();
      o.setMetricName("ExtensionRequestQueueSize");
      o.setUnit(StandardUnit.Count);
      o.setValue((double)extCtrl.getQueueSize());
      o.setDimensions(this.dimensions);
      this.data.add(o);
   }

   public void run() {
      this.data.clear();
      this.reportUserCount();
      this.reportMemory();
      this.reportThreads();
      PutMetricDataRequest putMetricDataRequest = new PutMetricDataRequest();
      putMetricDataRequest.setMetricData(this.data);
      putMetricDataRequest.setNamespace("Smartfox");
      this.cloudwatch.putMetricData(putMetricDataRequest);
   }
}
