package com.capillary.social.utils;

import java.util.Arrays;
import java.util.List;

/**
 * Created by rajeev on 23/11/17.
 */
public class GoogleReportParameter extends GoogleParameter {

    public static enum Attribute {
        AccountCurrencyCode, AccountDescriptiveName, AccountTimeZone, AdvertisingChannelSubType, AdvertisingChannelType, Amount, BaseCampaignId, BiddingStrategyId, BiddingStrategyName, BiddingStrategyType, BidType, BudgetId, CampaignDesktopBidModifier, CampaignGroupId, CampaignId, CampaignMobileBidModifier, CampaignName, CampaignStatus, CampaignTabletBidModifier, CampaignTrialType, CustomerDescriptiveName, EndDate, EnhancedCpcEnabled, ExternalCustomerId, IsBudgetExplicitlyShared, LabelIds, Labels, Period, ServingStatus, StartDate, TrackingUrlTemplate, UrlCustomParameters
    }

    public static enum Segment {
//        Date, DayOfWeek, Device, Month, MonthOfYear, Quarter, Week, Year, HourOfDay, Slot
    }

    public static enum Metric {
        ActiveViewCpm, ActiveViewCtr, ActiveViewImpressions, ActiveViewMeasurability, ActiveViewMeasurableCost, ActiveViewMeasurableImpressions, ActiveViewViewability, AllConversionRate, AllConversions, AllConversionValue, AverageCost, AverageCpc, AverageCpe, AverageCpm, AverageCpv, AveragePosition, ClickAssistedConversions, ClickAssistedConversionsOverLastClickConversions, ClickAssistedConversionValue, Clicks, ContentBudgetLostImpressionShare, ContentImpressionShare, ContentRankLostImpressionShare, ConversionRate, Conversions, ConversionValue, Cost, CostPerAllConversion, CostPerConversion, CostPerCurrentModelAttributedConversion, CrossDeviceConversions, Ctr, CurrentModelAttributedConversions, CurrentModelAttributedConversionValue, EngagementRate, Engagements, GmailForwards, GmailSaves, GmailSecondaryClicks, ImpressionAssistedConversions, ImpressionAssistedConversionsOverLastClickConversions, ImpressionAssistedConversionValue, ImpressionReach, Impressions, InteractionRate, Interactions, InteractionTypes, InvalidClickRate, InvalidClicks, NumOfflineImpressions, NumOfflineInteractions, OfflineInteractionRate, PercentNewVisitors, RelativeCtr, SearchBudgetLostImpressionShare, SearchExactMatchImpressionShare, SearchImpressionShare, SearchRankLostImpressionShare, ValuePerAllConversion, ValuePerConversion, ValuePerCurrentModelAttributedConversion, VideoQuartile100Rate, VideoQuartile25Rate, VideoQuartile50Rate, VideoQuartile75Rate, VideoViewRate, VideoViews, ViewThroughConversions, AverageFrequency, AveragePageviews, AverageTimeOnSite, BounceRate
    }

    public static String[] getAttributes() {
        return getNames(Attribute.class);
    }

    public static String[] getSegments() {
        return getNames(Segment.class);
    }

    public static String[] getMetric() {
        return getNames(Metric.class);
    }

}
