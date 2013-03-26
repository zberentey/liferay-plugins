/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.socialnetworking.friends.social;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.User;
import com.liferay.portal.security.permission.PermissionChecker;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portlet.social.model.BaseSocialActivityInterpreter;
import com.liferay.portlet.social.model.SocialActivity;
import com.liferay.socialnetworking.util.PortletPropsValues;
import com.liferay.socialnetworking.util.SocialNetworkingUtil;

/**
 * @author Brian Wing Shun Chan
 */
public class FriendsActivityInterpreter extends BaseSocialActivityInterpreter {

	public String[] getClassNames() {
		return _CLASS_NAMES;
	}

	@Override
	protected String getLink(
			SocialActivity activity, ServiceContext serviceContext)
		throws Exception {

		StringBundler sb = new StringBundler(4);

		sb.append(serviceContext.getPortalURL());
		sb.append(serviceContext.getPathFriendlyURLPublic());
		sb.append(StringPool.SLASH);

		User creatorUser = UserLocalServiceUtil.getUserById(
			activity.getUserId());

		sb.append(HtmlUtil.escapeURL(creatorUser.getScreenName()));

		return sb.toString();
	}

	@Override
	protected Object[] getTitleArguments(
			String groupName, SocialActivity activity, String link,
			String title, ServiceContext serviceContext)
		throws Exception {

		int activityType = activity.getType();

		if (activityType != FriendsActivityKeys.ADD_FRIEND) {
			return new Object[0];
		}

		String creatorUserNameURL = getUserProfileURL(
			activity.getUserId(), serviceContext);

		String receiverUserNameURL = getUserProfileURL(
			activity.getReceiverUserId(), serviceContext);

		return new Object[] {creatorUserNameURL, receiverUserNameURL};
	}

	@Override
	protected String getTitlePattern(
		String groupName, SocialActivity activity) {

		int activityType = activity.getType();

		if (activityType == FriendsActivityKeys.ADD_FRIEND) {
			return "activity-social-networking-summary-add-friend";
		}

		return StringPool.BLANK;
	}

	@Override
	protected boolean hasPermissions(
		PermissionChecker permissionChecker, SocialActivity activity,
		String actionId, ServiceContext serviceContext) {

		return true;
	}

	private String getUserProfileURL(long userId, ServiceContext serviceContext)
		throws PortalException, SystemException {

		String urlPattern = PortletPropsValues.FRIENDS_USER_PROFILE_URL;

		if (Validator.isNull(urlPattern)) {
			return getUserName(userId, serviceContext);
		}

		User user = UserLocalServiceUtil.getUserById(userId);

		StringBundler sb = new StringBundler(3);

		sb.append(serviceContext.getPortalURL());
		sb.append(PortalUtil.getPathContext());
		sb.append(SocialNetworkingUtil.replaceVariables(urlPattern, user));

		return wrapLink(sb.toString(), HtmlUtil.escape(user.getFullName()));
	}

	private static final String[] _CLASS_NAMES = {User.class.getName()};

}