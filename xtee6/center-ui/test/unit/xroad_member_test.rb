require 'test_helper'

class XRoadMemberTest < ActiveSupport::TestCase

  test "Should load all requests" do
    # Given
    query_params = ListQueryParams.new(
        "requests.created_at", "desc", 0, 10)

    # When
    requests = XRoadMember.get_management_requests(
        "riigiasutus", "member_in_vallavalitsused", query_params)

    # Then
    assert_equal(2, requests.size)
  end

  test "Should find requests with offset" do
    # Given
    query_params = ListQueryParams.new(
        "requests.created_at", "desc", 1, 10)

    # When
    requests = XRoadMember.get_management_requests(
        "riigiasutus", "member_in_vallavalitsused", query_params)

    # Then
    assert_equal(1, requests.size)

    request = requests[0]
    assert_equal("CENTER", request.origin)
    assert_equal("member_in_vallavalitsused", request.security_server.member_code)
  end

  test "Should find requests with limit" do
    # Given
    query_params = ListQueryParams.new(
        "requests.created_at", "desc", 0, 1)

    # When
    requests = XRoadMember.get_management_requests(
        "riigiasutus", "member_in_vallavalitsused", query_params)

    # Then
    assert_equal(1, requests.size)

    request = requests[0]
    assert_equal("SECURITY_SERVER", request.origin)
    assert_equal("member_in_vallavalitsused", request.sec_serv_user.member_code)
  end

  test "Should update request server owner name when member name changed" do
    # Given
    member = XRoadMember.where(:member_code => "member_in_vallavalitsused").first
    new_name = "New name"

    # When
    member.update_attributes!(:name => new_name)

    # Then
    updated_request = Request.where(
        :server_owner_class => "riigiasutus",
        :server_owner_code => "member_in_vallavalitsused").first

    assert_equal(new_name, updated_request.server_owner_name)
  end

  test "Should update request server user name when member name changed" do
    # Given
    member = XRoadMember.where(:member_code => "member_out_of_vallavalitsused").first
    new_name = "New name"

    # When
    member.update_attributes!(:name => new_name)

    # Then
    updated_request = Request.where(
        :server_owner_class => "riigiasutus",
        :server_owner_code => "member_in_vallavalitsused").first

    assert_equal(new_name, updated_request.server_user_name)
  end

  test "Should preserve server owner name when owner deleted" do
    # Given
    member = XRoadMember.where(:member_code => "member_in_vallavalitsused").first

    # When
    member.destroy

    # Then
    last_name = "This member should belong to group 'vallavalitsused'"
    updated_request = Request.where(
        :server_owner_class => "riigiasutus",
        :server_owner_code => "member_in_vallavalitsused").first

    assert_equal(last_name, updated_request.server_owner_name)
  end

  test "Should preserve server user name when user deleted" do
    # Given
    member = XRoadMember.where(:member_code => "member_out_of_vallavalitsused").first

    # When
    member.destroy

    # Then
    last_name = "This member should NOT belong to group 'vallavalitsused'"
    updated_request = Request.where(
        :server_owner_class => "riigiasutus",
        :server_owner_code => "member_in_vallavalitsused").first

    assert_equal(last_name, updated_request.server_user_name)
  end
end
