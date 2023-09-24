require 'json'

package = JSON.parse(File.read(File.join(__dir__, 'package.json')))

Pod::Spec.new do |s|
	s.name = 'CapacitorTwitterxLogin'
	s.version = '0.0.1'
	s.summary = 'authenticate with twitterx'
	s.license = 'MIT'
	s.homepage = 'https://github.com/GerritHoskins/capacitor-twitterx-login'
	s.author = 'Stewan Silva'
	s.source = { :git => 'https://github.com/GerritHoskins/capacitor-twitterx-login', :tag => package['name'] + '@' + package['version'] }
	s.source_files = 'ios/Plugin/**/*.{swift,h,m,c,cc,mm,cpp}', 'preferences/ios/Plugin/**/*.{swift,h,m,c,cc,mm,cpp}'
	s.ios.deployment_target  = '13.0'
	s.dependency 'Capacitor'
	s.swift_version = '5.1'
	s.dependency 'TwitterKit5'
end
