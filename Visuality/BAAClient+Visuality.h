//
//  BAAClient+Visuality.h
//  Markab
//
//  Created by Michele Longhi on 23/05/14.
//  Copyright (c) 2014 Visuality srl. All rights reserved.
//

#import "BAAClient.h"

@interface BAAClient (Visuality)

- (void)grantAccessToCollection:(NSString *)collectionName
                       objectId:(NSString *)objectId
                         toRole:(NSString *)roleName
                     accessType:(NSString *)access
                     completion:(BAAObjectResultBlock)completionBlock;

- (void)loadDictionaryObjectsFromCollection:(NSString *)collectionName
                                 withParams:(NSDictionary *)parameters
                                 completion:(BAAArrayResultBlock)completionBlock;

@end
