package com.capillary.social.utils;

import java.util.Arrays;
import java.util.List;

/**
 * Created by rajeev on 23/11/17.
 */
public class GoogleReportParameter extends GoogleParameter {

    public static enum Attribute {
//        AccountCurrencyCode, AccountDescriptiveName, AccountTimeZone, AdvertisingChannelSubType, AdvertisingChannelType, Amount, BaseCampaignId, BiddingStrategyId, BiddingStrategyName, BiddingStrategyType, BidType, BudgetId, CampaignDesktopBidModifier, CampaignGroupId, CampaignId, CampaignMobileBidModifier, CampaignName, CampaignStatus, CampaignTabletBidModifier, CampaignTrialType, CustomerDescriptiveName, EndDate, EnhancedCpcEnabled, ExternalCustomerId, IsBudgetExplicitlyShared, LabelIds, Labels, Period, ServingStatus, StartDate, TrackingUrlTemplate, UrlCustomParameters // CAMPAIGN_PERFORMANCE_REPORT
        AccentColor, AccountCurrencyCode, AccountDescriptiveName, AccountTimeZone, AdGroupId, AdGroupName, AdGroupStatus, AdType, AllowFlexibleColor, Automated, BaseAdGroupId, BaseCampaignId, BusinessName, CallOnlyPhoneNumber, CallToActionText, CampaignId, CampaignName, CampaignStatus, CombinedApprovalStatus, CreativeDestinationUrl, CreativeFinalAppUrls, CreativeFinalMobileUrls, CreativeFinalUrls, CreativeTrackingUrlTemplate, CreativeUrlCustomParameters, CriterionType, CustomerDescriptiveName, Description, Description1, Description2, DevicePreference, DisplayUrl, EnhancedDisplayCreativeLandscapeLogoImageMediaId, EnhancedDisplayCreativeLogoImageMediaId, EnhancedDisplayCreativeMarketingImageMediaId, EnhancedDisplayCreativeMarketingImageSquareMediaId, ExternalCustomerId, FormatSetting, Headline, HeadlinePart1, HeadlinePart2, Id, ImageAdUrl, ImageCreativeImageHeight, ImageCreativeImageWidth, ImageCreativeMimeType, ImageCreativeName, IsNegative, LabelIds, Labels, LongHeadline, MainColor, Path1, Path2, PolicySummary, PricePrefix, PromoText, ShortHeadline, Status // AD_PERFORMANCE_REPORT
    }

    public static enum Segment {
//        Date, DayOfWeek, Device, Month, MonthOfYear, Quarter, Week, Year, HourOfDay, Slot // CAMPAIGN_PERFORMANCE_REPORT
//        AdNetworkType1, AdNetworkType2, ClickType, ConversionCategoryName, ConversionTrackerId, ConversionTypeName, CriterionId, Date, DayOfWeek, Device, ExternalConversionSource, Month, MonthOfYear, Quarter, Slot, Week, Year // AD_PERFORMANCE_REPORT
    }

    public static enum Metric {
//        ActiveViewCpm, ActiveViewCtr, ActiveViewImpressions, ActiveViewMeasurability, ActiveViewMeasurableCost, ActiveViewMeasurableImpressions, ActiveViewViewability, AllConversionRate, AllConversions, AllConversionValue, AverageCost, AverageCpc, AverageCpe, AverageCpm, AverageCpv, AveragePosition, ClickAssistedConversions, ClickAssistedConversionsOverLastClickConversions, ClickAssistedConversionValue, Clicks, ContentBudgetLostImpressionShare, ContentImpressionShare, ContentRankLostImpressionShare, ConversionRate, Conversions, ConversionValue, Cost, CostPerAllConversion, CostPerConversion, CostPerCurrentModelAttributedConversion, CrossDeviceConversions, Ctr, CurrentModelAttributedConversions, CurrentModelAttributedConversionValue, EngagementRate, Engagements, GmailForwards, GmailSaves, GmailSecondaryClicks, ImpressionAssistedConversions, ImpressionAssistedConversionsOverLastClickConversions, ImpressionAssistedConversionValue, ImpressionReach, Impressions, InteractionRate, Interactions, InteractionTypes, InvalidClickRate, InvalidClicks, NumOfflineImpressions, NumOfflineInteractions, OfflineInteractionRate, PercentNewVisitors, RelativeCtr, SearchBudgetLostImpressionShare, SearchExactMatchImpressionShare, SearchImpressionShare, SearchRankLostImpressionShare, ValuePerAllConversion, ValuePerConversion, ValuePerCurrentModelAttributedConversion, VideoQuartile100Rate, VideoQuartile25Rate, VideoQuartile50Rate, VideoQuartile75Rate, VideoViewRate, VideoViews, ViewThroughConversions, AverageFrequency, AveragePageviews, AverageTimeOnSite, BounceRate // CAMPAIGN_PERFORMANCE_REPORT
        ActiveViewCpm, ActiveViewCtr, ActiveViewImpressions, ActiveViewMeasurability, ActiveViewMeasurableCost, ActiveViewMeasurableImpressions, ActiveViewViewability, AllConversionRate, AllConversions, AllConversionValue, AverageCost, AverageCpc, AverageCpe, AverageCpm, AverageCpv, AveragePageviews, AveragePosition, AverageTimeOnSite, BounceRate, ClickAssistedConversions, ClickAssistedConversionsOverLastClickConversions, ClickAssistedConversionValue, Clicks, ConversionRate, Conversions, ConversionValue, Cost, CostPerAllConversion, CostPerConversion, CostPerCurrentModelAttributedConversion, CrossDeviceConversions, Ctr, CurrentModelAttributedConversions, CurrentModelAttributedConversionValue, EngagementRate, Engagements, GmailForwards, GmailSaves, GmailSecondaryClicks, ImpressionAssistedConversions, ImpressionAssistedConversionsOverLastClickConversions, ImpressionAssistedConversionValue, Impressions, InteractionRate, Interactions, InteractionTypes, PercentNewVisitors, ValuePerAllConversion, ValuePerConversion, ValuePerCurrentModelAttributedConversion, VideoQuartile100Rate, VideoQuartile25Rate, VideoQuartile50Rate, VideoQuartile75Rate, VideoViewRate, VideoViews, ViewThroughConversions // AD_PERFORMANCE_REPORT
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
