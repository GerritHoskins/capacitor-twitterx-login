package = JSON.parse(File.read(File.join(__dir__, 'package.json')))

Pod::Spec.new do |s|
  s.name = 'CapacitorTwitterXLogin'
  s.version = package['version']
  s.summary = package['description']
  s.license = package['license']
  s.homepage = 'https://github.com/GerritHoskins/capacitor-twitterx-login'
  s.author = package['author']
  s.source = { :git => 'https://github.com/GerritHoskins/capacitor-twitterx-login', :tag => package['name'] + '@' + package['version'] }
  s.source_files = 'ios/Plugin/**/*.{swift,h,m,c,cc,mm,cpp}', 'capacitor-twitterx-login/ios/Plugin/**/*.{swift,h,m,c,cc,mm,cpp}'
  s.ios.deployment_target  = '13.0'
  s.dependency 'Capacitor'
  s.swift_version = '5.1'
end